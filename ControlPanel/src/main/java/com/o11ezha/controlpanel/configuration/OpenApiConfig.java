package com.o11ezha.controlpanel.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Тестовое API для управления задачами.", version = "v1"))
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("authenticated")
                .packagesToScan("com.o11ezha.controlpanel.controller.v1")
                .pathsToMatch("/v1/**")
                .pathsToExclude("/v1/auth/register", "/v1/auth/login")
                .build();
    }

    @Bean
    public GroupedOpenApi noAuthApi() {
        return GroupedOpenApi.builder()
                .group("no-auth")
                .pathsToMatch("/v1/auth/register", "/v1/auth/login")
                .build();
    }
}