package com.coffee.americanote.config;


import com.coffee.americanote.security.handler.CustomAccessDeniedHandler;
import com.coffee.americanote.security.handler.CustomAuthenticationEntryPoint;
import com.coffee.americanote.security.jwt.filter.JwtAuthenticationFilter;
import com.coffee.americanote.security.jwt.filter.JwtFailureFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
class SecurityConfig implements WebMvcConfigurer {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtFailureFilter jwtFailureFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .headers(configure -> configure.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable).disable())
                .sessionManagement(configure ->
                        configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests((request) ->
                        request.requestMatchers(
                                        new AntPathRequestMatcher("/"),
                                        new AntPathRequestMatcher("/api/**"),
                                        new AntPathRequestMatcher("/swagger-ui.html"),
                                        new AntPathRequestMatcher("/swagger-ui/**"),
                                        new AntPathRequestMatcher("/api-docs/**"),
                                        new AntPathRequestMatcher("/error"),
                                        new AntPathRequestMatcher("/favicon.ico")
                                ).permitAll()
                                .anyRequest().authenticated())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFailureFilter, JwtAuthenticationFilter.class)

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler));

        return http.build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://americanote.vercel.app", "https://americanote.store")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true) // 자격 증명 옵션 사용 시 Origins * 사용 불가, 명시적인 도메인 필요
                .maxAge(3600);
    }
}