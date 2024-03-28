package com.coffee.americanote.coffee.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "커피 API 명세서",
                description = "Americanote 커피 관련 API 명세서",
                version = "v1"
        )
)
@Configuration
public class CoffeeSwaggerConfig {

    @Bean
    GroupedOpenApi coffeeDocs() {
        return GroupedOpenApi.builder()
                .group("Coffee API")
                .packagesToScan("com.coffee.americanote.coffee.controller")
                .build();
    }
}
