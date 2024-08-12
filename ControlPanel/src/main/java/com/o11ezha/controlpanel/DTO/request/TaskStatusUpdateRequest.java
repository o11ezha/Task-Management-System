package com.o11ezha.controlpanel.DTO.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusUpdateRequest {
    @Pattern(regexp = "TODO|INPROGRESS|DONE",
            message = "Статус задания должен быть TODO, INPROGRESS или DONE.")
    private String taskStatus;
}
