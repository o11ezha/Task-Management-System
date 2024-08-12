package com.o11ezha.controlpanel.DTO.model;

import com.o11ezha.controlpanel.DTO.enums.TaskPriority;
import com.o11ezha.controlpanel.DTO.enums.TaskStatus;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskModel {
    private UUID taskId;
    private UserModel taskOwner;
    private UserModel taskPerformer;
    private TaskPriority taskPriority;
    private TaskStatus taskStatus;
    private String taskName;
    private String taskTheme;
    private String taskDesc;
}
