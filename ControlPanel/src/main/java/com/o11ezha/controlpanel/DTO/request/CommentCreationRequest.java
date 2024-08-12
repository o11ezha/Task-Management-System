package com.o11ezha.controlpanel.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreationRequest {
    @NotBlank
    @Size(min = 1, max = 256, message = "Длина ФИО не должна быть меньше 1 символов и больше 256.")
    private String commentText;
}
