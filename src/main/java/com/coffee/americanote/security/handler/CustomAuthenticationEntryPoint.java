package com.coffee.americanote.security.handler;

import com.coffee.americanote.common.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.coffee.americanote.common.entity.ErrorCode.SECURITY_UNAUTHORIZED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorResponse errorResponse = new ErrorResponse(SECURITY_UNAUTHORIZED, SECURITY_UNAUTHORIZED.getErrorMessage());

        log.error("{} is occurred in CustomAuthenticationEntryPoint", errorResponse.errorCode());

        response.setStatus(FORBIDDEN.value());
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
