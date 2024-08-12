package com.o11ezha.controlpanel.service.comment;

import com.o11ezha.controlpanel.DTO.model.CommentModel;
import com.o11ezha.controlpanel.DTO.request.CommentCreationRequest;
import com.o11ezha.controlpanel.exception.comment.*;
import com.o11ezha.controlpanel.exception.task.TaskNotFoundException;
import com.o11ezha.controlpanel.exception.user.UserNotATaskOwner;
import com.o11ezha.controlpanel.exception.user.UserNotFoundException;

import java.util.List;

public interface CommentService {

    List<CommentModel> getTaskComments(String taskId) throws CommentListGetException, TaskNotFoundException;

    String createComment(String taskId, CommentCreationRequest commentCreationRequest, String email) throws InvalidCommentCreationDataException, TaskNotFoundException, UserNotFoundException, CommentCreationException;

    void deleteComment(String commentId, String email) throws UserNotFoundException, CommentNotFoundException, UserNotATaskOwner, CommentDeletionException;
}
