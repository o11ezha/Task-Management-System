package com.o11ezha.controlpanel.DTO.filter;

import com.o11ezha.controlpanel.DTO.enums.TaskPriority;
import com.o11ezha.controlpanel.DTO.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskFilterCriteria {
    private String taskName;
    private UUID taskOwner;
    private UUID taskPerformer;
    private TaskPriority taskPriority;
    private TaskStatus taskStatus;
}
