package com.o11ezha.controlpanel.DAO;

import com.o11ezha.controlpanel.DTO.filter.TaskFilterCriteria;
import com.o11ezha.controlpanel.DTO.filter.TaskSpecification;
import com.o11ezha.controlpanel.DTO.model.TaskModel;
import com.o11ezha.controlpanel.DTO.model.UserModel;
import com.o11ezha.controlpanel.entity.TaskEntity;
import com.o11ezha.controlpanel.entity.UserEntity;
import com.o11ezha.controlpanel.exception.task.TaskNotFoundException;
import com.o11ezha.controlpanel.mapper.TaskMapper;
import com.o11ezha.controlpanel.mapper.UserMapper;
import com.o11ezha.controlpanel.repository.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskDAO {

    private final TaskRepo taskRepo;

    public List<TaskModel> getAllTasks() {
        return taskRepo.findAll().stream().map(TaskMapper.INSTANCE::taskToTaskModel).toList();
    }

    public Page<TaskModel> getFilteredTasks(TaskFilterCriteria criteria, Pageable pageable) {
        Specification<TaskEntity> spec = Specification.where(TaskSpecification.hasTaskName(criteria.getTaskName()))
                .and(TaskSpecification.hasTaskPriority(criteria.getTaskPriority()))
                .and(TaskSpecification.hasTaskOwner(criteria.getTaskOwner()))
                .and(TaskSpecification.hasTaskPerformer(criteria.getTaskPerformer()))
                .and(TaskSpecification.hasTaskStatus(criteria.getTaskStatus()));

        Page<TaskEntity> taskPage = taskRepo.findAll(spec, pageable);
        return taskPage.map(TaskMapper.INSTANCE::taskToTaskModel);
    }

    public List<TaskModel> getAllUserAsOwnerTasks(UserModel userModel){
        UserEntity user = UserMapper.INSTANCE.userDTOToUser(userModel);
        return taskRepo.getAllUserAsOwnerTasks(user).stream().map(TaskMapper.INSTANCE::taskToTaskModel).toList();
    }

    public List<TaskModel> getAllUserAsPerformerTasks(UserModel userModel) {
        UserEntity user = UserMapper.INSTANCE.userDTOToUser(userModel);
        return taskRepo.getAllUserAsPerformerTasks(user).stream().map(TaskMapper.INSTANCE::taskToTaskModel).toList();
    }

    public TaskModel getTask(UUID taskId) throws TaskNotFoundException {
        TaskEntity task = taskRepo.getTaskByID(taskId).orElseThrow(() -> new TaskNotFoundException(String.format("Задание с id '%s' не найдено", taskId)));
        return TaskMapper.INSTANCE.taskToTaskModel(task);
    }

    public TaskModel createTask(TaskModel taskModel) {
        TaskEntity task = TaskMapper.INSTANCE.taskDTOToTaskWithoutIdAndPerformer(taskModel);
        TaskEntity savedTask = taskRepo.save(task);
        return TaskMapper.INSTANCE.taskToTaskModel(savedTask);
    }

    public void deleteTask(UUID taskId) throws TaskNotFoundException {
        TaskEntity task = taskRepo.getTaskByID(taskId).orElseThrow(() -> new TaskNotFoundException(String.format("Задание с id '%s' не найдено", taskId)));
        taskRepo.delete(task);
    }

    public TaskModel updateTask(TaskModel taskModel) throws TaskNotFoundException {
        TaskEntity task = TaskMapper.INSTANCE.taskDTOToTask(taskModel);
        return TaskMapper.INSTANCE.taskToTaskModel(taskRepo.save(task));
    }
}
