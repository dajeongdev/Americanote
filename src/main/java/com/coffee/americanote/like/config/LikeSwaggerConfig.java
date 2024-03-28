package com.coffee.americanote.like.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "좋아요 API 명세서",
                description = "Americanote 좋아요 관련 API 명세서",
                version = "v1"
        )
)
@Configuration
class LikeSwaggerConfig {

    @Bean
    GroupedOpenApi likeDocs() {
        return GroupedOpenApi.builder()
                .group("Like API")
                .packagesToScan("com.coffee.americanote.like.controller")
                .build();
    }
}
