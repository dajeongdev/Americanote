package com.coffee.americanote.like.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
