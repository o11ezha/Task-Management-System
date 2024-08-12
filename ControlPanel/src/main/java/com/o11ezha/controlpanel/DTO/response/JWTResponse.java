package com.o11ezha.controlpanel.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTResponse {
    private UUID id;
    private String email;
    private String token;
    private String type = "Bearer";

    public JWTResponse(UUID id, String email, String token) {
        this.id = id;
        this.email = email;
        this.token = token;
    }
}
