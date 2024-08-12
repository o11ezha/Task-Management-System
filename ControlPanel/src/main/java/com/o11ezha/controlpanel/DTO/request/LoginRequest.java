package com.o11ezha.controlpanel.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Email(message = "Электронная почта не соответствует требованиям.")
    private String email;

    @NotBlank(message = "Пароль не должен быть пустым.")
    private String password;
}
