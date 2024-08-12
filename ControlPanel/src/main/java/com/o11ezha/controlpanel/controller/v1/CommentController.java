package com.o11ezha.controlpanel.controller.v1;

import com.o11ezha.controlpanel.DTO.request.CommentCreationRequest;
import com.o11ezha.controlpanel.DTO.response.CommentCreationResponse;
import com.o11ezha.controlpanel.DTO.response.CommentListResponse;
import com.o11ezha.controlpanel.exception.comment.*;
import com.o11ezha.controlpanel.exception.task.TaskNotFoundException;
import com.o11ezha.controlpanel.exception.user.UserNotATaskOwner;
import com.o11ezha.controlpanel.exception.user.UserNotFoundException;
import com.o11ezha.controlpanel.security.userdetails.ControlPanelUserDetails;
import com.o11ezha.controlpanel.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{taskid}")
    @Operation(summary = "Вывод комментариев к заданию.",
            description = "Возвращает список комментариев к определённому по ID заданию.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список комментариев получен.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = CommentListResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Задание не найдено.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Задание с id '' не найдено.\" }"))),
                    @ApiResponse(responseCode = "403", description = "Пользователь не имеет прав на получение комментариев.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"\"Full authentication is required to access this.\" }")))})
    public ResponseEntity<CommentListResponse> getTaskComments(
                                                @Parameter(description = "ID задания, к которому необходимо получить комментарии.", required = true)
                                                @PathVariable("taskid") String taskId) throws CommentListGetException, TaskNotFoundException {
        return ResponseEntity.ok(new CommentListResponse(commentService.getTaskComments(taskId)));
    }

    @PostMapping("/{taskid}")
    @Operation(summary = "Написание комментария.",
            description = "Создаёт комментарий к определённому по ID заданию.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Комментарий успешно создан.",
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = CommentCreationResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Комментарий не найден.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Комментарий с id '' не найден.\" }"))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные комментария.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Текст комментария пустой.\" }"))),
                    @ApiResponse(responseCode = "403", description = "Пользователь не имеет прав на создание комментариев.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"\"Full authentication is required to access this.\" }")))})
    public ResponseEntity<CommentCreationResponse> createComment(@AuthenticationPrincipal ControlPanelUserDetails userDetails,
                                                    @Valid @RequestBody CommentCreationRequest commentCreationRequest,
                                                    @Parameter(description = "ID задания, к которому необходимо получить комментарии.", required = true)
                                                    @PathVariable("taskid") String taskId) throws UserNotFoundException, InvalidCommentCreationDataException, TaskNotFoundException, CommentCreationException {
        String email = userDetails.getEmail();
        CommentCreationResponse commentCreationResponse = new CommentCreationResponse(commentService.createComment(taskId, commentCreationRequest, email));
        return ResponseEntity.status(HttpStatus.CREATED).body(commentCreationResponse);
    }

    @DeleteMapping("/{commentid}")
    @Operation(summary = "Удаление комментария.",
            description = "Удаляет определённый по ID комментарий.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Комментарий успешно удалён."),
                    @ApiResponse(responseCode = "404", description = "Комментарий или задание не найдено.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Комментарий с id '' не найден.\" }"))),
                    @ApiResponse(responseCode = "403", description = "Пользователь не имеет прав на удаление комментариев.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"\"Full authentication is required to access this.\" }")))})
    public ResponseEntity<HttpStatus> deleteComment(@AuthenticationPrincipal ControlPanelUserDetails userDetails,
                                                    @Parameter(description = "ID комментария, который необходимо удалить.", required = true)
                                                    @PathVariable("commentid") String commentId) throws UserNotFoundException, UserNotATaskOwner, CommentDeletionException, CommentNotFoundException {
        String email = userDetails.getEmail();
        commentService.deleteComment(commentId, email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
