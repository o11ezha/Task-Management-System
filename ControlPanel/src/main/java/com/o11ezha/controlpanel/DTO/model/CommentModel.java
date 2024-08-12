package com.o11ezha.controlpanel.DTO.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentModel {
    private UUID commentId;
    private UserModel commentOwner;
    private TaskModel commentToTaskId;
    private String commentText;
}
