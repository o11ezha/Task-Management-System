package com.o11ezha.controlpanel.controller.v1;

import com.o11ezha.controlpanel.DTO.request.LoginRequest;
import com.o11ezha.controlpanel.DTO.request.RegistrationRequest;
import com.o11ezha.controlpanel.DTO.response.JWTResponse;
import com.o11ezha.controlpanel.exception.user.InvalidRegistrationDataException;
import com.o11ezha.controlpanel.exception.user.RegistrationException;
import com.o11ezha.controlpanel.exception.user.UserAlreadyExistsException;
import com.o11ezha.controlpanel.exception.user.UserNotFoundException;
import com.o11ezha.controlpanel.security.jwt.JwtUtils;
import com.o11ezha.controlpanel.security.userdetails.ControlPanelUserDetails;
import com.o11ezha.controlpanel.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    @Operation(
            summary = "Регистрация нового пользователя.",
            description = "Регистрирует нового пользователя в системе.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Пользователь зарегистрирован."),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные регистрации.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Почта или пароль пустые.\" }"))),
                    @ApiResponse(responseCode = "409", description = "Пользователь уже существует.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Пользователь с почтой '' уже существует.\" }"))),
                    @ApiResponse(responseCode = "500", description = "Ошибка регистрации пользователя.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Ошибка на сервере.\" }")))})
    public ResponseEntity<HttpStatus> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) throws RegistrationException, UserAlreadyExistsException, InvalidRegistrationDataException, UserNotFoundException {
        userService.registerUser(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(
            summary = "Аутентификация пользователя.",
            description = "Аутентифицирует пользователя и возвращает JWT токен при успешной аутентификации.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь успешно аутентифицирован.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JWTResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Неверные учетные данные.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Неверный email или пароль.\" }"))),
                    @ApiResponse(responseCode = "500", description = "Ошибка аутентификации.",
                            content = @Content(schema = @Schema(type = "object", example = "{ \"message\": \"Ошибка на сервере.\" }")))})
    public ResponseEntity<JWTResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtTokenForUser(authentication);
        ControlPanelUserDetails userDetails = (ControlPanelUserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JWTResponse(userDetails.getId(),
                userDetails.getUsername(),
                jwt));
    }
}
