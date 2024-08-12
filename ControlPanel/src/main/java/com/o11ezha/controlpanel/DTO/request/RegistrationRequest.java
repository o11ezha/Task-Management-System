package com.o11ezha.controlpanel.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    @Email(message = "Электронная почта не соответствует требованиям.")
    private String email;

    @Size(min = 2, max = 128, message = "Длина ФИО не должна быть меньше 2 символов и больше 128.")
    private String fullName;

    @NotBlank(message = "Пароль не должен быть пустым.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
            message = "Пароль не соответствует требованиям (хотя бы: 1 цифра, 1 прописанная буква, 1 заглавная буква, 1 спец. символ, длина от 8 до 20 символов).")
    private String password;
}
