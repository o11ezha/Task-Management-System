package com.o11ezha.controlpanel.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreationRequest {

    @NotBlank(message = "Название задания не должно быть пустым.")
    @Size(min = 2, max = 64, message = "Длина названия задания не должна быть меньше 2 символов и больше 64.")
    private String taskName;

    @NotBlank(message = "Тема задания не должна быть пустой.")
    @Size(min = 2, max = 64, message = "Длина темы задания не должна быть меньше 2 символов и больше 64.")
    private String taskTheme;

    @NotBlank(message = "Описание задания не должно быть пустым.")
    @Size(min = 2, max = 256, message = "Длина описания задания не должна быть меньше 2 символов и больше 256.")
    private String taskDesc;

    @NotBlank(message = "Приоритет задания не должен быть пустым.")
    @Pattern(regexp = "HIGH|MEDIUM|LOW",
            message = "Приоритет задания должен быть HIGH, MEDIUM или LOW.")
    private String taskPriority;
}
