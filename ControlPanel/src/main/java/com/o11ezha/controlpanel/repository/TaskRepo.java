package com.o11ezha.controlpanel.repository;

import com.o11ezha.controlpanel.entity.TaskEntity;
import com.o11ezha.controlpanel.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepo extends JpaRepository<TaskEntity, UUID>, JpaSpecificationExecutor<TaskEntity> {

    @Query("""
            SELECT t
            FROM TaskEntity t
            WHERE t.taskId = :taskId
            """)
    Optional<TaskEntity> getTaskByID(@Param("taskId") UUID taskId);

    @Query("""
            SELECT t
            FROM TaskEntity t
            WHERE t.taskOwner = :owner
            """)
    List<TaskEntity> getAllUserAsOwnerTasks(@Param("owner") UserEntity owner);

    @Query("""
            SELECT t
            FROM TaskEntity t
            WHERE t.taskPerformer = :performer
            """)
    List<TaskEntity> getAllUserAsPerformerTasks(@Param("performer") UserEntity performer);

    Page<TaskEntity> findAll(Specification<TaskEntity> spec, Pageable pageable);

}
