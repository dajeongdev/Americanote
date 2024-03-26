package com.coffee.americanote.user.controller;

import com.coffee.americanote.user.domain.request.KakaoLoginRequest;
import com.coffee.americanote.user.domain.response.LoginResponse;
import com.coffee.americanote.user.oauth2.KakaoLoginUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "User", description = "사용자 관련 API입니다.")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final KakaoLoginUtil kakaoLoginUtil;

    @GetMapping("/user/kakao")
    ResponseEntity<LoginResponse> login(@RequestParam("code") String code) {
        // 인가 코드 받아서 토큰 조회 후 사용자 정보 조회 (임시)
        KakaoLoginRequest kakaoLoginRequest = kakaoLoginUtil.kakaoOAuth(code);

        // TODO 조회해 온 정보로 회원 조회 및 가입

        // TODO 토큰 생성
        String accessToken = "";

        // TODO 토큰 넣기
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        return new ResponseEntity<>(null, headers, HttpStatus.OK);
    }
}