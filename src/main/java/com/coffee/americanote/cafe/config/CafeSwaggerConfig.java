package com.coffee.americanote.cafe.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CafeSwaggerConfig {

    @Bean
    GroupedOpenApi cafeDocs() {
        return GroupedOpenApi.builder()
                .group("Cafe API")
                .packagesToScan("com.coffee.americanote.cafe.controller")
                .build();
    }
}