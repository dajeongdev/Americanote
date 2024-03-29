package com.coffee.americanote.mypage.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "마이페이지 API 명세서",
                description = "Americanote 마이페이지 관련 API 명세서",
                version = "v1"
        )
)
@Configuration
class MypageSwaggerConfig {

    @Bean
    GroupedOpenApi mypageDocs() {
        return GroupedOpenApi.builder()
                .group("Mypage API")
                .packagesToScan("com.coffee.americanote.mypage.controller")
                .build();
    }
}
