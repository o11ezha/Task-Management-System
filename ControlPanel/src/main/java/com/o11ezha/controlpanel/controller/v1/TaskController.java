package com.o11ezha.controlpanel.controller.v1;

import com.o11ezha.controlpanel.DTO.enums.TaskPriority;
import com.o11ezha.controlpanel.DTO.enums.TaskStatus;
import com.o11ezha.controlpanel.DTO.filter.TaskFilterCriteria;
import com.o11ezha.controlpanel.DTO.model.TaskModel;
import com.o11ezha.controlpanel.DTO.request.TaskCreationRequest;
import com.o11ezha.controlpanel.DTO.request.TaskDetailsUpdateRequest;
import com.o11ezha.controlpanel.DTO.request.TaskStatusUpdateRequest;
import com.o11ezha.controlpanel.DTO.response.TaskCreationResponse;
import com.o11ezha.controlpanel.DTO.response.TaskResponse;
import com.o11ezha.controlpanel.DTO.response.TaskUpdateResponse;
import com.o11ezha.controlpanel.DTO.response.TasksListResponse;
import com.o11ezha.controlpanel.exception.task.*;
import com.o11ezha.controlpanel.exception.user.UserNotATaskOwner;
import com.o11ezha.controlpanel.exception.user.UserNotFoundException;
import com.o11ezha.controlpanel.security.userdetails.ControlPanelUserDetails;
import com.o11ezha.controlpanel.service.task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final PagedResourcesAssembler<TaskModel> pagedResourcesAssembler;

    @GetMapping("")
    @Operation(summary = "Вывод всех заданий.", description = "Возвращает список всех заданий.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список заданий получен.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = TasksListResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Пользователь не имеет прав на вывод задач.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"\"Full authentication is required to access this.\" }")))})
    public ResponseEntity<TasksListResponse> getAllTasks() throws TaskGetException {
        return ResponseEntity.ok(new TasksListResponse(taskService.getAllTasks()));
    }

    @GetMapping("/filter")
    @Operation(summary = "Вывод заданий с фильтрацией.", description = "Возвращает отфильтрованный список заданий на основе параметров фильтрации.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список отфильтрованных заданий получен.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = PagedModel.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметра фильтра.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Invalid parameter.\" }"))),
                    @ApiResponse(responseCode = "403", description = "Пользователь не имеет прав на вывод задач.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"\"Full authentication is required to access this.\" }")))})
    public ResponseEntity<PagedModel<EntityModel<TaskModel>>> getFilteredTasks(@RequestParam(value = "taskname", required = false) @Size(min = 2, max = 64, message = "Длина названия задания не должна быть меньше 2 символов и больше 64.") String taskName,
                                                                      @RequestParam(value = "owner",  required = false) @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$") String owner,
                                                                      @RequestParam(value = "performer", required = false) @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$") String performer,
                                                                      @RequestParam(value = "priority", required = false) @Pattern(regexp = "HIGH|MEDIUM|LOW", message = "Приоритет задания должен быть HIGH, MEDIUM или LOW.") String priority,
                                                                      @RequestParam(value = "status", required = false) @Pattern(regexp = "TODO|INPROGRESS|DONE", message = "Статус задания должен быть TODO, INPROGRESS или DONE.") String status,
                                                                      @RequestParam(value = "page", defaultValue = "0") @Min(value = 0, message = "Page number должно быть больше или равно 0.") int page,
                                                                      @RequestParam(value = "size", defaultValue = "10") @Min(value = 1, message = "Size должно быть больше 0.") int size) {
        TaskFilterCriteria.TaskFilterCriteriaBuilder criteriaBuilder = TaskFilterCriteria.builder()
                .taskName(taskName);

        if (priority != null) {
            criteriaBuilder.taskPriority(TaskPriority.valueOf(priority));
        }
        if (owner != null) {
            criteriaBuilder.taskOwner(UUID.fromString(owner));
        }
        if (performer != null) {
            criteriaBuilder.taskPerformer(UUID.fromString(performer));
        }
        if (status != null) {
            criteriaBuilder.taskStatus(TaskStatus.valueOf(status));
        }

        TaskFilterCriteria criteria = criteriaBuilder.build();
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskModel> tasks = taskService.getFilteredTasks(criteria, pageable);

        PagedModel<EntityModel<TaskModel>> pagedModel = pagedResourcesAssembler.toModel(tasks);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping(value = "/{userid}", consumes = "application/json;type=owner")
    @Operation(summary = "Вывод всех заданий пользователя как владельца.", description = "Возвращает список заданий пользователя, в которых он является владельцем.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список заданий пользователя как владельца получен.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = TasksListResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Пользователь с id '' не найден.\" }"))),
                    @ApiResponse(responseCode = "403", description = "Пользователь не имеет прав на вывод задач.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"\"Full authentication is required to access this.\" }")))})
    public ResponseEntity<TasksListResponse> getAllUserAsOwnerTasks(@Parameter(description = "ID пользователя", required = true) @PathVariable("userid") String userId) throws UserNotFoundException, TaskGetException {
        return ResponseEntity.ok(new TasksListResponse(taskService.getAllUserAsOwnerTasks(userId)));
    }

    @GetMapping(value ="/{userid}", consumes = "application/json;type=performer")
    @Operation(summary = "Вывод всех заданий пользователя как исполнителя.", description = "Возвращает список заданий пользователя, в которых он является исполнителем.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список заданий пользователя как исполнителя получен.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TasksListResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Пользователь с id '' не найден.\" }"))),
                    @ApiResponse(responseCode = "403", description = "Пользователь не имеет прав на вывод задач.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"\"Full authentication is required to access this.\" }")))})
    public ResponseEntity<TasksListResponse> getAllUserAsPerformerTasks(@PathVariable("userid") String userId) throws UserNotFoundException, TaskGetException {
        return ResponseEntity.ok(new TasksListResponse(taskService.getAllUserAsPerformerTasks(userId)));
    }

    @GetMapping("/{taskid}")
    @Operation(summary = "Получение задания по ID.", description = "Возвращает определённое по ID задание.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Информация о задании получена.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Задание не найдена.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Задание с id '' не найдено.\" }"))),
                    @ApiResponse(responseCode = "403", description = "Пользователь не имеет прав на вывод задач.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"\"Full authentication is required to access this.\" }")))})
    public ResponseEntity<TaskResponse> getTask(@Parameter(description = "ID задачи", required = true) @PathVariable("taskid") String taskId) throws UserNotFoundException, TaskGetException, TaskNotFoundException {
        return ResponseEntity.ok(new TaskResponse(taskService.getTask(taskId)));
    }

    @PostMapping("")
    @Operation(summary = "Создание задач.", description = "Создает новую задачу.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Задача успешно создана.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TaskCreationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные для создания задачи.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Один из полей создания задания пустые.\" }"))),
                    @ApiResponse(responseCode = "403", description = "Пользователь не имеет прав на создание задачи.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"\"Full authentication is required to access this.\" }")))})
    public ResponseEntity<TaskCreationResponse> createTask(@AuthenticationPrincipal ControlPanelUserDetails userDetails,
                                                           @Valid @RequestBody TaskCreationRequest taskCreationRequest) throws UserNotFoundException, InvalidTaskCreationDataException, TaskCreationException {
        String email = userDetails.getEmail();
        TaskCreationResponse taskCreationResponse = new TaskCreationResponse(taskService.createTask(taskCreationRequest, email));
        return ResponseEntity.status(HttpStatus.CREATED).body(taskCreationResponse);
    }

    @PutMapping(value = "/{taskid}", consumes = "application/json;type=status")
    @Operation(summary = "Обновление статуса задачи.", description = "Обновляет статус существующей задачи.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Статус задачи обновлен.",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = TaskUpdateResponse.class))),
                @ApiResponse(responseCode = "400", description = "Некорректные данные для обновления статуса задачи.",
                        content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Статус задания должен быть TODO, INPROGRESS или DONE.\" }"))),
                @ApiResponse(responseCode = "404", description = "Задача не найдена.",
                        content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Задание с id '' не найдено.\" }"))),
                @ApiResponse(responseCode = "403", description = "Пользователь не имеет прав на изменение задачи.",
                        content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"\"Full authentication is required to access this.\" }")))})
    public ResponseEntity<TaskUpdateResponse> updateTaskStatus(@AuthenticationPrincipal ControlPanelUserDetails userDetails,
                                                               @Parameter(description = "ID задачи", required = true) @PathVariable("taskid") String taskId,
                                                               @Valid @RequestBody TaskStatusUpdateRequest taskStatusUpdateRequest) throws UserNotFoundException, UserNotATaskOwner, TaskUpdateException, TaskNotFoundException {
        String email = userDetails.getEmail();
        return ResponseEntity.ok(new TaskUpdateResponse(taskService.updateTaskStatus(taskId, taskStatusUpdateRequest, email)));
    }

    @PutMapping(value = "/{taskid}", consumes = "application/json;type=details")
    @Operation(summary = "Обновление характеристик задачи.", description = "Обновляет характеристик существующей задачи.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Характеристики задачи обновлены.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TaskUpdateResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные для обновления статуса задачи.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Некорректные данные для обновления статуса задачи.\" }"))),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Задание с id '' не найдено.\" }"))),
                    @ApiResponse(responseCode = "403", description = "Пользователь не имеет прав на изменение задачи.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"\"Full authentication is required to access this.\" }")))})
    public ResponseEntity<TaskUpdateResponse> updateTaskDetails(@AuthenticationPrincipal ControlPanelUserDetails userDetails,
                                                                @Parameter(description = "ID задачи", required = true) @PathVariable("taskid") String taskId,
                                                                @Valid @RequestBody TaskDetailsUpdateRequest taskDetailsUpdateRequest) throws UserNotFoundException, UserNotATaskOwner, TaskUpdateException, TaskNotFoundException {
        String email = userDetails.getEmail();
        return ResponseEntity.ok(new TaskUpdateResponse(taskService.updateTaskDetails(taskId, taskDetailsUpdateRequest, email)));
    }

    @DeleteMapping("/{taskid}")
    @Operation(summary = "Удаление задачи.",
            description = "Удаляет определённую по ID задачу.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Задание успешно удалено."),
                    @ApiResponse(responseCode = "404", description = "Задание не найдено.",
                                    content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Задание с id '' не найдено.\" }"))),
                    @ApiResponse(responseCode = "403", description = "Пользователь не имеет прав на удаление задачи.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"\"Full authentication is required to access this.\" }")))})
    public ResponseEntity<HttpStatus> deleteTask(@AuthenticationPrincipal ControlPanelUserDetails userDetails,
                                                 @Parameter(description = "ID задачи", required = true) @PathVariable("taskid") String taskId) throws UserNotFoundException, UserNotATaskOwner, TaskNotFoundException, TaskDeletionException {
        String email = userDetails.getEmail();
        taskService.deleteTask(taskId, email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
