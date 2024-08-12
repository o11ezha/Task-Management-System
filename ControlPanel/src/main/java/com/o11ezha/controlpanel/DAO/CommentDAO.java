package com.o11ezha.controlpanel.DAO;

import com.o11ezha.controlpanel.DTO.model.CommentModel;
import com.o11ezha.controlpanel.DTO.model.TaskModel;
import com.o11ezha.controlpanel.entity.CommentEntity;
import com.o11ezha.controlpanel.entity.TaskEntity;
import com.o11ezha.controlpanel.exception.comment.CommentNotFoundException;
import com.o11ezha.controlpanel.mapper.CommentMapper;
import com.o11ezha.controlpanel.mapper.TaskMapper;
import com.o11ezha.controlpanel.repository.CommentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommentDAO {

    private final CommentRepo commentRepo;

    public List<CommentModel> getTaskComments(TaskModel taskModel) {
        TaskEntity task = TaskMapper.INSTANCE.taskDTOToTask(taskModel);
        return commentRepo.getCommentByTaskID(task).stream().map(CommentMapper.INSTANCE::commentToCommentModel).toList();
    }

    public CommentModel getComment(UUID commentId) throws CommentNotFoundException {
        CommentEntity comment = commentRepo.getCommentByID(commentId).orElseThrow(() -> new CommentNotFoundException(String.format("Комментарий с id '%s' не найден", commentId)));
        return CommentMapper.INSTANCE.commentToCommentModel(comment);
    }

    public CommentModel createComment(CommentModel commentModel) {
        CommentEntity comment =  CommentMapper.INSTANCE.commentDTOToCommentWithoutId(commentModel);
        CommentEntity savedComment = commentRepo.save(comment);
        return CommentMapper.INSTANCE.commentToCommentModel(savedComment);
    }

    public void deleteComment(UUID commentId) throws CommentNotFoundException {
        CommentEntity comment = commentRepo.getCommentByID(commentId).orElseThrow(() -> new CommentNotFoundException(String.format("Комментарий с id '%s' не найден", commentId)));
        commentRepo.delete(comment);
    }
}
