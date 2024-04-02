package com.coffee.americanote.cafe.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
