package com.o11ezha.controlpanel.service.task;

import com.o11ezha.controlpanel.DTO.filter.TaskFilterCriteria;
import com.o11ezha.controlpanel.DTO.model.TaskModel;
import com.o11ezha.controlpanel.DTO.request.TaskCreationRequest;
import com.o11ezha.controlpanel.DTO.request.TaskDetailsUpdateRequest;
import com.o11ezha.controlpanel.DTO.request.TaskStatusUpdateRequest;
import com.o11ezha.controlpanel.exception.task.*;
import com.o11ezha.controlpanel.exception.user.UserNotATaskOwner;
import com.o11ezha.controlpanel.exception.user.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    List<TaskModel> getAllTasks() throws TaskGetException;

    Page<TaskModel> getFilteredTasks(TaskFilterCriteria criteria, Pageable pageable);

    List<TaskModel> getAllUserAsOwnerTasks(String userId) throws TaskGetException, UserNotFoundException;

    List<TaskModel> getAllUserAsPerformerTasks(String userId) throws TaskGetException, UserNotFoundException;

    TaskModel getTask(String taskId) throws TaskGetException, UserNotFoundException, TaskNotFoundException;

    String createTask(TaskCreationRequest taskCreationRequest, String email) throws InvalidTaskCreationDataException, UserNotFoundException, TaskCreationException;

    TaskModel updateTaskStatus(String taskId, TaskStatusUpdateRequest taskStatusUpdateRequest, String email) throws UserNotFoundException, TaskNotFoundException, UserNotATaskOwner, TaskUpdateException;

    TaskModel updateTaskDetails(String taskId, TaskDetailsUpdateRequest taskDetailsUpdateRequest, String email) throws UserNotFoundException, TaskNotFoundException, UserNotATaskOwner, TaskUpdateException;

    void deleteTask(String taskId, String email) throws UserNotFoundException, TaskNotFoundException, UserNotATaskOwner, TaskDeletionException;
}
