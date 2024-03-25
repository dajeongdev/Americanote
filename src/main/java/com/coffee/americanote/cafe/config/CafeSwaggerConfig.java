package com.coffee.americanote.cafe.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CafeSwaggerConfig {

    @Bean
    OpenApiCustomizer cafeOpenApiCustomizer() {
        return openApi -> openApi
                .addServersItem(
                        new Server()
                                .url("http://localhost:8009")
                                .description("local")
                ).setInfo(
                        new Info()
                                .title("카페 API 명세서")
                                .description("Americanote 카페 API 명세서")
                );
    }

    @Bean
    GroupedOpenApi cafeDocs() {
        return GroupedOpenApi.builder()
                .group("Cafe API")
                .packagesToScan("com.coffee.americanote.cafe.controller")
                .build();
    }
}
