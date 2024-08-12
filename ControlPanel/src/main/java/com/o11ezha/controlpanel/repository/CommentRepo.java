package com.o11ezha.controlpanel.repository;

import com.o11ezha.controlpanel.entity.CommentEntity;
import com.o11ezha.controlpanel.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepo extends JpaRepository<CommentEntity, UUID> {

    @Query("""
            SELECT c
            FROM CommentEntity c
            WHERE c.commentToTaskId = :task
            """)
    List<CommentEntity> getCommentByTaskID(@Param("task") TaskEntity task);

    @Query("""
            SELECT c
            FROM CommentEntity c
            WHERE c.commentId = :commentId
            """)
    Optional<CommentEntity> getCommentByID(@Param("commentId") UUID commentId);
}
