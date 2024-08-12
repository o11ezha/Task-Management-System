package com.o11ezha.controlpanel.DTO.response;

import com.o11ezha.controlpanel.DTO.model.TaskModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private TaskModel task;
}
