package com.o11ezha.controlpanel.DTO.response;

import com.o11ezha.controlpanel.DTO.model.TaskModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TasksListResponse {
    private List<TaskModel> tasks;
}
