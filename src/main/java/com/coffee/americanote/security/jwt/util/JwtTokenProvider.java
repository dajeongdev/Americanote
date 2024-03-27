package com.coffee.americanote.security.jwt.util;

import com.coffee.americanote.common.entity.ErrorCode;
import com.coffee.americanote.common.exception.TokenException;
import com.coffee.americanote.user.domain.entity.UserToken;
import com.coffee.americanote.user.service.UserTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.coffee.americanote.common.entity.ErrorCode.EXPIRED_TOKEN;

@Slf4j
@Component
public class JwtTokenProvider {

    private final UserTokenService userTokenService;

    private static final long ACCESS_TOKEN_EXPIRED_TIME = Duration.ofHours(1).toMillis(); // 60분
    private static final long REFRESH_TOKEN_EXPIRED_TIME = Duration.ofDays(14).toDays(); // 2주
    private static final String KEY_ROLE = "role";

    private static final String TOKEN_PREFIX = "Bearer ";

    private final SecretKey secretKey;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String key,
                            UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
        this.secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String createAccessToken(Authentication authentication) {
        return TOKEN_PREFIX + createToken(authentication, ACCESS_TOKEN_EXPIRED_TIME);
    }

    public String createRefreshToken(Authentication authentication) {
        return TOKEN_PREFIX + createToken(authentication, REFRESH_TOKEN_EXPIRED_TIME);
    }

    private String createToken(Authentication authentication, long expiredTime) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expiredTime);

        Claims claims = Jwts.claims().setSubject(authentication.getName()); // name == userId
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put(KEY_ROLE, authorities);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new TokenException(EXPIRED_TOKEN);
        }
        return parseClaims(token)
                .getExpiration()
                .after(new Date());
    }

    public String reissueAccessToken(String accessToken) {
        UserToken token = userTokenService.findTokenByAccessToken(accessToken);
        String refreshToken = token.getRefreshToken();

        if (!validateToken(refreshToken)) {
            throw new TokenException(EXPIRED_TOKEN);
        }

        String reissuedAccessToken = TOKEN_PREFIX + createAccessToken(getAuthentication(refreshToken));
        userTokenService.updateToken(token, reissuedAccessToken);
        return reissuedAccessToken;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException e) {
            throw new TokenException(ErrorCode.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new TokenException(ErrorCode.INTERNAL_ERROR);
        }
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);
        User userDetails = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, accessToken, authorities);
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Collections.singletonList(
                new SimpleGrantedAuthority(claims.get(KEY_ROLE).toString()));
    }
}