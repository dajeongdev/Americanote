package com.coffee.americanote.mypage.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
