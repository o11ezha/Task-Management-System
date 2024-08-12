package com.o11ezha.controlpanel.service.task;

import com.o11ezha.controlpanel.DAO.TaskDAO;
import com.o11ezha.controlpanel.DAO.UserDAO;
import com.o11ezha.controlpanel.DTO.enums.TaskPriority;
import com.o11ezha.controlpanel.DTO.enums.TaskStatus;
import com.o11ezha.controlpanel.DTO.filter.TaskFilterCriteria;
import com.o11ezha.controlpanel.DTO.model.TaskModel;
import com.o11ezha.controlpanel.DTO.model.UserModel;
import com.o11ezha.controlpanel.DTO.request.TaskCreationRequest;
import com.o11ezha.controlpanel.DTO.request.TaskDetailsUpdateRequest;
import com.o11ezha.controlpanel.DTO.request.TaskStatusUpdateRequest;
import com.o11ezha.controlpanel.exception.task.*;
import com.o11ezha.controlpanel.exception.user.UserNotATaskOwner;
import com.o11ezha.controlpanel.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskDAO taskDAO;
    private final UserDAO userDAO;

    @Override
    public List<TaskModel> getAllTasks() throws TaskGetException {
        try {
           return taskDAO.getAllTasks();
        }
        catch (Exception e) {
            throw new TaskGetException("Произошла следующая ошибка при выводе заданий: ", e);
        }
    }

    @Override
    public Page<TaskModel> getFilteredTasks(TaskFilterCriteria criteria, Pageable pageable) {
        return taskDAO.getFilteredTasks(criteria, pageable);
    }

    @Override
    public List<TaskModel> getAllUserAsOwnerTasks(String userId) throws TaskGetException, UserNotFoundException {
        UserModel userModel = userDAO.getUserById(UUID.fromString(userId));
        try {
            return taskDAO.getAllUserAsOwnerTasks(userModel);
        }
        catch (Exception e) {
            throw new TaskGetException("Произошла следующая ошибка при выводе заданий: ", e);
        }
    }

    @Override
    public List<TaskModel> getAllUserAsPerformerTasks(String userId) throws TaskGetException, UserNotFoundException {
        UserModel userModel = userDAO.getUserById(UUID.fromString(userId));
        try {
            return taskDAO.getAllUserAsPerformerTasks(userModel);
        } catch (Exception e) {
            throw new TaskGetException("Произошла следующая ошибка при выводе заданий: ", e);
        }
    }

    @Override
    public TaskModel getTask(String taskId) throws TaskNotFoundException {
        return taskDAO.getTask(UUID.fromString(taskId));
    }

    @Override
    public String createTask(TaskCreationRequest taskCreationRequest, String email) throws InvalidTaskCreationDataException, UserNotFoundException, TaskCreationException {
        if (taskCreationRequest.getTaskName() == null || taskCreationRequest.getTaskTheme() == null ||
                taskCreationRequest.getTaskDesc() == null || taskCreationRequest.getTaskPriority() == null ) {
            throw new InvalidTaskCreationDataException("Один из полей создания задания пустые.");
        }

        if (!userDAO.existsByEmail(email)) {
            throw new UserNotFoundException(String.format("Пользователя с почтой '%s' не существует.", email));
        }

        UserModel userModel = userDAO.getUserByEmail(email);

        try {
            TaskModel taskModel = TaskModel.builder()
                    .taskName(taskCreationRequest.getTaskName())
                    .taskTheme(taskCreationRequest.getTaskTheme())
                    .taskDesc(taskCreationRequest.getTaskDesc())
                    .taskPriority(TaskPriority.valueOf(taskCreationRequest.getTaskPriority()))
                    .taskOwner(userModel)
                    .taskStatus(TaskStatus.TODO)
                    .build();
            return String.valueOf(taskDAO.createTask(taskModel).getTaskId());
        } catch (Exception e) {
            throw new TaskCreationException("Произошла следующая ошибка при создании задания: ", e);
        }
    }

    @Override
    public TaskModel updateTaskStatus(String taskId, TaskStatusUpdateRequest taskStatusUpdateRequest, String email) throws UserNotFoundException, TaskNotFoundException, UserNotATaskOwner, TaskUpdateException {
        if (!userDAO.existsByEmail(email)) {
            throw new UserNotFoundException(String.format("Пользователя с почтой '%s' не существует.", email));
        }

        TaskModel taskModel = taskDAO.getTask(UUID.fromString(taskId));
        try {
            if (taskModel.getTaskOwner().getEmail().equals(email)) {
                taskModel.setTaskStatus(TaskStatus.valueOf(taskStatusUpdateRequest.getTaskStatus()));
                return taskDAO.updateTask(taskModel);
            } else {
                throw new UserNotATaskOwner(String.format("Пользователь с почтой '%s' не является автором задания с Id '%s'", email, taskId));
            }
        } catch (Exception e) {
            throw new TaskUpdateException("Произошла следующая ошибка при обновлении задания: ", e);
        }
    }

    @Override
    public TaskModel updateTaskDetails(String taskId, TaskDetailsUpdateRequest taskDetailsUpdateRequest, String email) throws UserNotFoundException, TaskNotFoundException, UserNotATaskOwner, TaskUpdateException {
        if (!userDAO.existsByEmail(email)) {
            throw new UserNotFoundException(String.format("Пользователя с почтой '%s' не существует.", email));
        }

        TaskModel taskModel = taskDAO.getTask((UUID.fromString(taskId)));
        try {
            if (taskModel.getTaskOwner().getEmail().equals(email)) {
                if (taskDetailsUpdateRequest.getEmailPerformer() != null) {
                    String emailPerformer = taskDetailsUpdateRequest.getEmailPerformer();
                    if (!userDAO.existsByEmail(emailPerformer)) {
                        throw new UserNotFoundException(String.format("Пользователя с почтой '%s' не существует.", emailPerformer));
                    }
                    taskModel.setTaskPerformer(userDAO.getUserByEmail(emailPerformer));
                    taskModel.setTaskStatus(TaskStatus.INPROGRESS);
                }

                if (taskDetailsUpdateRequest.getTaskName() != null) {
                    taskModel.setTaskName(taskDetailsUpdateRequest.getTaskName());
                }
                if (taskDetailsUpdateRequest.getTaskTheme() != null) {
                    taskModel.setTaskTheme(taskDetailsUpdateRequest.getTaskTheme());
                }

                if (taskDetailsUpdateRequest.getTaskDesc() != null) {
                    taskModel.setTaskDesc(taskDetailsUpdateRequest.getTaskDesc());
                }

                if (taskDetailsUpdateRequest.getTaskPriority() != null) {
                    taskModel.setTaskPriority(TaskPriority.valueOf(taskDetailsUpdateRequest.getTaskPriority()));
                }
                return taskDAO.updateTask(taskModel);
            } else {
                throw new UserNotATaskOwner(String.format("Пользователь с почтой '%s' не является автором задания с Id '%s'", email, taskId));
            }
        } catch (Exception e) {
            throw new TaskUpdateException("Произошла следующая ошибка при обновлении задания: ", e);
        }
    }

    @Override
    public void deleteTask(String taskId, String email) throws UserNotFoundException, TaskNotFoundException, UserNotATaskOwner, TaskDeletionException {
        if (!userDAO.existsByEmail(email)) {
            throw new UserNotFoundException(String.format("Пользователя с почтой '%s' не существует.", email));
        }

        TaskModel taskModel = taskDAO.getTask((UUID.fromString(taskId)));

        try {
            if (taskModel.getTaskOwner().getEmail().equals(email)) {
                taskDAO.deleteTask(taskModel.getTaskId());
            } else {
                throw new UserNotATaskOwner(String.format("Пользователь с почтой '%s' не является автором задания с Id '%s'", email, taskId));
            }
        } catch (Exception e) {
            throw new TaskDeletionException("Произошла следующая ошибка при удалении задания: ", e);
        }
    }
}
