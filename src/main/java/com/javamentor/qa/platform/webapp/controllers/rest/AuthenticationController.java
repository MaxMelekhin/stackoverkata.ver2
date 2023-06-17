package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AuthenticationRequestDTO;
import com.javamentor.qa.platform.models.dto.TokenResponseDTO;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.security.jwt.JwtTokenProvider;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Контроллер аутентификации")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/token")
    @Operation(summary = "Аутентифицировать пользователя и сгенерировать токен доступа")
    @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен")
    @ApiResponse(responseCode = "401", description = "Не правильный логин/пароль")
    @ApiResponse(responseCode = "404", description = "Пользователь с таким логином не найден")
    public ResponseEntity<TokenResponseDTO> authentication (AuthenticationRequestDTO authenticationRequest) {

        try {
            String login = authenticationRequest.getLogin();
            String pass = authenticationRequest.getPass();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, authenticationRequest.getPass()));
            User user = userService.getByEmail(login).orElseGet(User::new);

            if (user == null) {
                throw new UsernameNotFoundException("Пользователь с логином: " + login + " не найден");
            }

            List<Role> roles = new ArrayList<>();
            roles.add(user.getRole());

            String token = jwtTokenProvider.createToken(login, roles);

            TokenResponseDTO response = new TokenResponseDTO();
            response.setToken(token);
            response.setRole(user.getRole().getName());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Не правильный логин/пароль");
        }
    }

    @PostMapping("/logout")
    public void logout (HttpServletResponse response, HttpServletRequest request) {

    }
}