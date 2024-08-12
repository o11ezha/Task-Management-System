package com.o11ezha.controlpanel.handler;

import com.o11ezha.controlpanel.exception.comment.*;
import com.o11ezha.controlpanel.exception.task.*;
import com.o11ezha.controlpanel.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> buildErrorResponse(Exception e, HttpStatus status) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", e.getMessage());
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        return new ResponseEntity<>(errorDetails, status);
    }

    @ExceptionHandler(InvalidRegistrationDataException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRegistrationDataException(InvalidRegistrationDataException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Map<String, Object>> handleRegistrationException(RegistrationException e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return buildErrorResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotATaskOwner.class)
    public ResponseEntity<Map<String, Object>> handleUserNotATaskOwner(UserNotATaskOwner e) {
        return buildErrorResponse(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException e) {
        return buildErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTaskCreationDataException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidTaskCreationDataException(InvalidTaskCreationDataException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TaskCreationException.class)
    public ResponseEntity<Map<String, Object>> handleTaskCreationException(TaskCreationException e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TaskDeletionException.class)
    public ResponseEntity<Map<String, Object>> handleTaskDeletionException(TaskDeletionException e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TaskGetException.class)
    public ResponseEntity<Map<String, Object>> handleTaskGetException(TaskGetException e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTaskNotFoundException(TaskNotFoundException e) {
        return buildErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskUpdateException.class)
    public ResponseEntity<Map<String, Object>> handleTaskUpdateException(TaskUpdateException e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CommentCreationException.class)
    public ResponseEntity<Map<String, Object>> handleCommentCreationException(CommentCreationException e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CommentDeletionException.class)
    public ResponseEntity<Map<String, Object>> handleCommentDeletionException(CommentDeletionException e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CommentListGetException.class)
    public ResponseEntity<Map<String, Object>> handleCommentListGetException(CommentListGetException e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCommentNotFoundException(CommentNotFoundException e) {
        return buildErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCommentCreationDataException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCommentCreationDataException(InvalidCommentCreationDataException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        errorDetails.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());

        Map<String, String> fieldErrors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        errorDetails.put("message", "Validation failed");
        errorDetails.put("fieldErrors", fieldErrors);

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
    }
}
