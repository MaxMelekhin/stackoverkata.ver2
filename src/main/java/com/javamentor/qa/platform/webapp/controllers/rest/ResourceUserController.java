package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@ApiOperation("User API")
@RestController
@RequestMapping("/api/user")
public class ResourceUserController {

    private final UserDtoService userDtoService;

    public ResourceUserController(UserDtoService userDtoService) {
        this.userDtoService = userDtoService;
    }

    @Operation(summary = "Получение userDto", description = "Позволяет получить экземпляр userDto по id user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно получен экземпляр userDto", response = UserDto.class),
            @ApiResponse(code = 401, message = "Пользователь не авторизирован"),
            @ApiResponse(code = 403, message = "Доступ запрещен"),
            @ApiResponse(code = 404, message = "Ответ не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserDtoById(@PathVariable Long id) {
        return userDtoService.getUserDtoById(id)
                .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new UserDto(), HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Получение PageDto", description = "Позволяет получить пагинацию UserDto  по указанным параметрам. " +
            "pageNumber - номер страницы, по дефолту  = 1, " +
            "pageSize - количество объектов на странице, по дефолту = 10, " +
            "parameterName - имя поля для выборки, по дефолту \"id\", " +
            "parameterValue - значение поля выборки, по дефолту \"\" ")


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно получен экземпляр pageDto", response = PageDto.class),
            @ApiResponse(code = 401, message = "Пользователь не авторизирован"),
            @ApiResponse(code = 403, message = "Доступ запрещен"),
            @ApiResponse(code = 404, message = "Ответ не найден")
    })
    @GetMapping()
    public PageDto<UserDto> userDtoPage(@RequestParam(required = false, defaultValue = "1") int pageNumber,
                                        @RequestParam(required = false, defaultValue = "10") int pageSize,
                                        @RequestParam(required = false, defaultValue = "id") String parameterName,
                                        @RequestParam(required = false, defaultValue = "") String parameterValue
    ) {

        List<UserDto> listDto = userDtoService.getItems(User.class, parameterName, parameterValue, pageSize, pageNumber);
        Long totalCount = userDtoService.getTotalResultCount(User.class, parameterName, parameterValue);
        return new PageDto<>(
                pageNumber,
                Math.toIntExact(totalCount / pageSize),
                Math.toIntExact(totalCount),
                listDto,
                pageSize
        );
    }
}
