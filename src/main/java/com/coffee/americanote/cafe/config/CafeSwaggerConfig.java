package com.coffee.americanote.cafe.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "카페 API 명세서",
                description = "Americanote 카페 관련 API 명세서",
                version = "v1"
        )
)
@Configuration
public class CafeSwaggerConfig {

    @Bean
    GroupedOpenApi cafeDocs() {
        return GroupedOpenApi.builder()
                .group("Cafe API")
                .packagesToScan("com.coffee.americanote.cafe.controller")
                .build();
    }
}
