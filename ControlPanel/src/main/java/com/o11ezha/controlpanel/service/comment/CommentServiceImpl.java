package com.o11ezha.controlpanel.service.comment;

import com.o11ezha.controlpanel.DAO.CommentDAO;
import com.o11ezha.controlpanel.DAO.TaskDAO;
import com.o11ezha.controlpanel.DAO.UserDAO;
import com.o11ezha.controlpanel.DTO.model.CommentModel;
import com.o11ezha.controlpanel.DTO.model.TaskModel;
import com.o11ezha.controlpanel.DTO.model.UserModel;
import com.o11ezha.controlpanel.DTO.request.CommentCreationRequest;
import com.o11ezha.controlpanel.exception.comment.*;
import com.o11ezha.controlpanel.exception.task.*;
import com.o11ezha.controlpanel.exception.user.UserNotATaskOwner;
import com.o11ezha.controlpanel.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentDAO commentDAO;
    private final TaskDAO taskDAO;
    private final UserDAO userDAO;

    @Override
    public List<CommentModel> getTaskComments(String taskId) throws CommentListGetException, TaskNotFoundException {
        TaskModel taskModel = taskDAO.getTask(UUID.fromString(taskId));
        try {
            return commentDAO.getTaskComments(taskModel);
        }
        catch (Exception e) {
            throw new CommentListGetException("Произошла следующая ошибка при выводе комментариев: ", e);
        }
    }

    @Override
    public String createComment(String taskId, CommentCreationRequest commentCreationRequest, String email) throws InvalidCommentCreationDataException, TaskNotFoundException, UserNotFoundException, CommentCreationException {
        if (commentCreationRequest.getCommentText() == null) {
            throw new InvalidCommentCreationDataException("Текст комментария пустой.");
        }

        if (!userDAO.existsByEmail(email)) {
            throw new UserNotFoundException(String.format("Пользователя с почтой '%s' не существует.", email));
        }

        TaskModel taskModel = taskDAO.getTask(UUID.fromString(taskId));
        UserModel userModel = userDAO.getUserByEmail(email);

        try {
            CommentModel commentModel = CommentModel.builder()
                    .commentText(commentCreationRequest.getCommentText())
                    .commentOwner(userModel)
                    .commentToTaskId(taskModel)
                    .build();

            return String.valueOf(commentDAO.createComment(commentModel).getCommentId());
        } catch (Exception e) {
            throw new CommentCreationException("Произошла следующая ошибка при создании задания: ", e);
        }
    }

    @Override
    public void deleteComment(String commentId, String email) throws UserNotFoundException, CommentNotFoundException, UserNotATaskOwner, CommentDeletionException {
        if (!userDAO.existsByEmail(email)) {
            throw new UserNotFoundException(String.format("Пользователя с почтой '%s' не существует.", email));
        }

        CommentModel commentModel = commentDAO.getComment(UUID.fromString(commentId));

        try {
            if (commentModel.getCommentOwner().getEmail().equals(email)) {
                commentDAO.deleteComment(commentModel.getCommentId());
            } else {
                throw new UserNotATaskOwner(String.format("Пользователь с почтой '%s' не является автором комментария с Id '%s'", email, commentId));
            }
        } catch (Exception e) {
            throw new CommentDeletionException("Произошла следующая ошибка при удалении комментария: ", e);
        }
    }
}
