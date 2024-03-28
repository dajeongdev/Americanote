package com.coffee.americanote.user.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "사용자 API 명세서",
                description = "Americanote 사용자 관련 API 명세서",
                version = "v1"
        )
)
@Configuration
class UserSwaggerConfig {

    private static final String TOKEN_PREFIX = "Bearer";

    // JWT Authorize 설정
    @Bean
    public OpenAPI openAPI() {
        String securityJwtName = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securityJwtName);
        Components components = new Components()
                .addSecuritySchemes(securityJwtName, new SecurityScheme()
                        .name(securityJwtName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme(TOKEN_PREFIX)
                        .bearerFormat(HttpHeaders.AUTHORIZATION));

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components);
    }

    // 목차 생성, Swaggeer 내 동일한 메소드 이름 불가
    @Bean
    GroupedOpenApi userDocs() {
        return GroupedOpenApi.builder()
                .group("User API")
                .packagesToScan("com.coffee.americanote.user.controller")
                .build();
    }
}
