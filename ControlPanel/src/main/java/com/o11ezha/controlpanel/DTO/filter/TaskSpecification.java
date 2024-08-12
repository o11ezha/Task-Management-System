package com.o11ezha.controlpanel.DTO.filter;

import com.o11ezha.controlpanel.DTO.enums.TaskPriority;
import com.o11ezha.controlpanel.DTO.enums.TaskStatus;
import com.o11ezha.controlpanel.entity.TaskEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class TaskSpecification {
    public static Specification<TaskEntity> hasTaskName(String taskName) {
        return (root, query, cb) ->
                taskName != null ? cb.equal(root.get("taskName"), taskName) : null;
    }

    public static Specification<TaskEntity> hasTaskPriority(TaskPriority priority) {
        return (root, query, cb) ->
                priority != null ? cb.equal(root.get("taskPriority"), priority) : null;
    }

    public static Specification<TaskEntity> hasTaskOwner(UUID taskOwner) {
        return (root, query, cb) ->
                taskOwner != null ? cb.equal(root.get("taskOwner").get("userId"), taskOwner) : null;
    }

    public static Specification<TaskEntity> hasTaskPerformer(UUID taskPerformer) {
        return (root, query, cb) ->
                taskPerformer != null ? cb.equal(root.get("taskPerformer").get("userId"), taskPerformer) : null;
    }

    public static Specification<TaskEntity> hasTaskStatus(TaskStatus status) {
        return (root, query, cb) ->
                status != null ? cb.equal(root.get("taskStatus"), status) : null;
    }
}
