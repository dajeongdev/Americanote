package com.coffee.americanote.security.jwt.filter;

import com.coffee.americanote.security.jwt.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String[] WHITELIST = {
            "/swagger-ui.html", // swagger
            "/swagger-ui/**", // swagger
            "/v3/api-docs/**", // swagger
            "/docs/**", // swagger
            "/error",
            "/favicon.ico",
            "/api/**",
    };
    private static final String HEADER_STRING = "Authorization";
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        log.info("path = {}", path);
        if (Arrays.stream(WHITELIST).anyMatch(pattern -> antPathMatcher.match(pattern, path))) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = resolveToken(request);
        validateAndReissueToken(accessToken);
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        return request.getHeader(HEADER_STRING).substring(7);
    }

    private void validateAndReissueToken(String accessToken) {
        if (jwtTokenProvider.validateToken(accessToken)) {
            setAuthentication(accessToken);
        } else {
            String reissueAccessToken = jwtTokenProvider.reissueAccessToken(accessToken);
            if (reissueAccessToken != null) {
                setAuthentication(reissueAccessToken);
            }
        }
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
