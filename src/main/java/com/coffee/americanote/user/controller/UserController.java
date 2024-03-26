package com.coffee.americanote.user.controller;

import com.coffee.americanote.user.domain.entity.UserToken;
import com.coffee.americanote.user.domain.response.LoginResponse;
import com.coffee.americanote.user.service.UserService;
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

    private final UserService userService;

    @GetMapping("/user/kakao")
    ResponseEntity<LoginResponse> login(@RequestParam("code") String code) {
        UserToken userToken = userService.getJwtToken(code);
        // TODO 토큰 넣기
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + userToken.getAccessToken());
        headers.add("refreshToken", "Bearer " + userToken.getRefreshToken());
        return new ResponseEntity<>(null, headers, HttpStatus.OK);
    }
}