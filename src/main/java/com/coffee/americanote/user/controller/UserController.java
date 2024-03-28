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

    public static final String HEADER_STRING = "Authorization";
    private final UserService userService;
    private final MyPageService myPageService;

    @Operation(summary = "summary : 카카오 로그인", description = "description : return header(accessToken)")
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200")
    @GetMapping("/kakao")
    ResponseEntity<Void> login(@RequestParam("code") String code, HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_STRING);
        if (accessToken == null) {
            accessToken = userService.getJwtToken(code);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        return new ResponseEntity<>(null, headers, HttpStatus.OK);
    }

    @Operation(summary = "summary : 로그아웃", description = "description : return ok")
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200")
    @PostMapping("/logout")
    ResponseEntity<Void> logout(HttpServletRequest request) {
        userService.logout(request.getHeader(HEADER_STRING));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @Operation(summary = "summary: 마이페이지 조회", description = "description : return user data")
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("/mypage")
    ResponseEntity<CommonResponse<UserResponse>> getMyData(HttpServletRequest request) {
        return new ResponseEntity<>(new CommonResponse<>(
                "마이페이지", myPageService.getMyData(request.getHeader(HEADER_STRING))), HttpStatus.OK);
    }

    @Operation(summary = "summary: 내 취향 커피 고르기", description = "description : return ok")
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200")
    @PutMapping("/choose/prefer")
    ResponseEntity<Void> updatePrefer(@RequestBody UserPreferRequest userPreferRequest, HttpServletRequest request){
        myPageService.updatePrefer(request.getHeader(HEADER_STRING), userPreferRequest);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}