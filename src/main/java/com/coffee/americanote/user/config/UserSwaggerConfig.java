package com.coffee.americanote.user.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserSwaggerConfig {

    @Bean
    OpenApiCustomizer userOpenApiCustomizer() {
        return openApi -> openApi
                .addServersItem(
                        new Server()
                                .url("http://localhost:8009")
                                .description("local")
                ).setInfo(
                        new Info()
                                .title("사용자 API 명세서")
                                .description("Americanote 사용자 API 명세서")
                );
    }

    @Bean
    GroupedOpenApi userDocs() {
        return GroupedOpenApi.builder()
                .group("User API")
                .packagesToScan("com.coffee.americanote.user.controller")
                .build();
    }
}
