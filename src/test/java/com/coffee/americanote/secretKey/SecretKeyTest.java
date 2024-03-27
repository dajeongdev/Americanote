package com.coffee.americanote.secretKey;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.util.Base64;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class SecretKeyTest {

    @Value("${jwt.secret-key}")
    private String secretKeyPlain;

    @Test
    void 시크릿키_존재_확인() {
        assertThat(secretKeyPlain).isNotNull();
    }

    @Test
    @DisplayName("sercretKey 원문으로 hmac 암호화 알고리즘에 맞는 SecretKey 객체를 만들 수 있다.")
    void t2() {
        // 키를 Base64 인코딩
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
        // Base64 인코딩된 키를 이용하여 SecretKey 객체를 만든다.
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
        assertThat(secretKey).isNotNull();
    }
}
