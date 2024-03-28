package com.coffee.americanote.user.controller;

import com.coffee.americanote.common.response.CommonResponse;
import com.coffee.americanote.user.domain.request.UserPreferRequest;
import com.coffee.americanote.user.domain.response.UserResponse;
import com.coffee.americanote.user.service.MyPageService;
import com.coffee.americanote.common.response.BasicApiSwaggerResponse;
import com.coffee.americanote.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "User", description = "사용자 관련 API입니다.")
@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final MyPageService myPageService;

    @Operation(summary = "summary : 카카오 로그인", description = "description : return header(accessToken)")
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("/kakao")
    ResponseEntity<Void> login(@RequestParam("code") String code, HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (accessToken == null) {
            accessToken = userService.getJwtToken(code);
        }
        log.info("accessToken = {}", accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        return new ResponseEntity<>(null, headers, HttpStatus.OK);
    }

    @Operation(summary = "summary: 마이페이지 조회", description = "description : return user data")
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("/mypage")
    ResponseEntity<CommonResponse<UserResponse>> getMyData(HttpServletRequest request) {
        return new ResponseEntity<>(new CommonResponse<>("마이페이지", myPageService.getMyData(request.getHeader("Authorization"))), HttpStatus.OK);
    }

    @Operation(summary = "summary: 내 취향 커피 고르기", description = "description : return ok")
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @PutMapping("/choose/prefer")
    ResponseEntity<Void> updatePrefer(@RequestBody UserPreferRequest userPreferRequest, HttpServletRequest request){
        myPageService.updatePrefer(request.getHeader("Authorization"), userPreferRequest);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}