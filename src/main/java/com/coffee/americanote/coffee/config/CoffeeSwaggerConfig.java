package com.coffee.americanote.coffee.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoffeeSwaggerConfig {

    @Bean
    OpenApiCustomizer coffeeOpenApiCustomizer() {
        return openApi -> openApi
                .addServersItem(
                        new Server()
                                .url("http://localhost:8009")
                                .description("local")
                ).setInfo(
                        new Info()
                                .title("커피 API 명세서")
                                .description("Americanote 커피 API 명세서")
                );
    }

    @Bean
    GroupedOpenApi coffeeDocs() {
        return GroupedOpenApi.builder()
                .group("Coffee API")
                .packagesToScan("com.coffee.americanote.coffee.controller")
                .build();
    }
}
