package com.coffee.americanote.user.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserSwaggerConfig {

    @Bean
    GroupedOpenApi userDocs() {
        return GroupedOpenApi.builder()
                .group("User API")
                .packagesToScan("com.coffee.americanote.user.controller")
                .build();
    }
}
