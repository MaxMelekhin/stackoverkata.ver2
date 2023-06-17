package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.converters.UserConverter;
import com.javamentor.qa.platform.models.dto.UserRegistrationDto;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.util.EncodeDecodeUserRegistrationDtoService;
import com.javamentor.qa.platform.service.abstracts.util.mail.EmailService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/user/registration")
@Tag(name = "Контроллер регистрации", description = "Верифицирует пользователя с помощью подтверждающей ссылки через email и сохраняет пользователяв БД")
public class RegistrationController {
    private Map<String, Long> tokenUserRegistration = new HashMap<>();
    private UserConverter INSTANCE = Mappers.getMapper(UserConverter.class );
    private final UserService userService;
    private final EncodeDecodeUserRegistrationDtoService userRegistrationDtoService;
    private final EmailService emailService;


    @Value("${registration.expiration.time.in.minutes}")
    private int EXPIRATION_TIME_IN_MINUTES;
    @Value("${spring.mail.username}")
    private String fromAddress;
    @Value("${registration.sender_name}")
    private String senderName;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${registration.url.verify}")
    private String URL_VERIFY;

    @Autowired
    public RegistrationController(UserService userService, EncodeDecodeUserRegistrationDtoService userRegistrationDtoService, EmailService emailService) {
        this.userService = userService;
        this.userRegistrationDtoService = userRegistrationDtoService;
        this.emailService = emailService;
    }

    @PostMapping
    @ApiOperation(value = "Генерирует сообщение, сожержащую ссылку на подтверждение почты")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Сообщение отправлено на почту"),
            @ApiResponse(code = 400, message = "Недопустимый запрос"),
            @ApiResponse(code = 409, message = "Данная почта зарегистрированна"),
            @ApiResponse(code = 500, message = "Ошибка генерации ссылки для подтверждения почты")
    })
    public ResponseEntity<String> sendMessage(@RequestBody @Valid UserRegistrationDto userRegistrationDto) throws IOException {
        if (userService.getByEmail(userRegistrationDto.getEmail()).isPresent()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        String userCode = userRegistrationDtoService.encode(userRegistrationDto);
        String userUrlVerify = URL_VERIFY + userCode;
        emailService.send(fromAddress, userRegistrationDto.getEmail(), senderName, userUrlVerify);
        tokenUserRegistration.put(userCode, System.currentTimeMillis());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify")
    @ApiOperation(value = "Проверяет верефицирующий код и записывает в БД")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Недопустимый запрос"),
            @ApiResponse(code = 409, message = "Данная почта зарегистрированна"),
            @ApiResponse(code = 500, message = "Ошибка расшифровки верифицирубщего кода")
    })
    public ResponseEntity<String> verify(@RequestParam(value = "verify", required = true) String verify) throws IOException, ClassNotFoundException {
        if (timeExpirationIsOver(verify)){
            return new ResponseEntity<>("время действия ссылки истекло", HttpStatus.CONFLICT);
        }

        UserRegistrationDto userRegistrationDto = userRegistrationDtoService.decode(verify);

        if (userService.getByEmail(userRegistrationDto.getEmail()).isPresent()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Role DEFAULT_ROLE = new Role("ROLE_USER");
        User createdUser = INSTANCE.userRegistrationDtoToUser(userRegistrationDto, DEFAULT_ROLE);
        userService.persist(createdUser);
        return ResponseEntity.ok().build();
    }

    private boolean timeExpirationIsOver(String verify){
        if (tokenUserRegistration.containsKey(verify)){
            int timeDif = (int) ((System.currentTimeMillis() - tokenUserRegistration.get(verify) / 60000));
            return timeDif > EXPIRATION_TIME_IN_MINUTES;
        }
        return false;
    }
}

