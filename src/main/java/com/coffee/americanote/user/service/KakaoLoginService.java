package com.coffee.americanote.user.service;

import com.coffee.americanote.user.domain.request.KakaoLoginRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class KakaoLoginService {

    @Value("${oauth.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${oauth.kakao.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${oauth.kakao.client-secret}")
    private String CLIENT_SECRET;

    public KakaoLoginRequest kakaoOAuth(String accessToken) {
        // 사용자 정보 조회
        ResponseEntity<String> kakaoProfile = getKakaoProfile(accessToken);

        // 사용자 객체 생성
        return getKakaoUserInfo(kakaoProfile);
    }

    private ResponseEntity<String> getKakaoProfile(String token) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(token);
        String accessToken = element.getAsJsonObject().get("access_token").getAsString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return new RestTemplate().exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                new HttpEntity<>(headers),
                String.class
        );
    }

    private KakaoLoginRequest getKakaoUserInfo(ResponseEntity<String> profile) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(profile.getBody());
        JsonElement properties = element.getAsJsonObject().get("properties");

        return KakaoLoginRequest.builder()
                .kakaoId(element.getAsJsonObject().get("id").getAsLong())
                .nickname(properties.getAsJsonObject().get("nickname").getAsString())
                .profileImageUrl(properties.getAsJsonObject().get("profile_image").getAsString())
                .build();
    }
}
