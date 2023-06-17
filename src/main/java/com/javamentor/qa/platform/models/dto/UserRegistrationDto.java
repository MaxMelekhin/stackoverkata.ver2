package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "ответ")
public class UserRegistrationDto implements Serializable {
    @Parameter(description = "Имя пользователя")
    @NotEmpty
    private String firstName;
    @Parameter(description = "Фамилия пользователя")
    @NotEmpty
    private String lastName;
    @Parameter(description = "Электронная почта пользователя")
    @NotEmpty
    private String email;
    @Parameter(description = "Пароль пользователя")
    @NotEmpty
    private String password;
}
