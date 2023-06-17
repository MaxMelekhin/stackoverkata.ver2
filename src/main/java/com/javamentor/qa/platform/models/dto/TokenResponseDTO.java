package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema (description = "Ответ на токен")
public class TokenResponseDTO {

    @Schema (description = "роль")
    @NotEmpty
    private String role;
    @Schema (description = "токен")
    @NotEmpty
    private String token;

}