package com.coffee.americanote.user.controller;

import com.coffee.americanote.common.response.BasicApiSwaggerResponse;
import com.coffee.americanote.common.response.CommonResponse;
import com.coffee.americanote.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "summary : 카카오 로그인",
            description = """
                    ## 요청 :
                    - header(Authorization Bearer *토큰* (옵션))
                    ## 응답 :
                    - 취향 여부(boolean)
                    """)
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200")
    @GetMapping("/kakao")
    ResponseEntity<CommonResponse<Boolean>> login(
            @RequestParam(value = "code", required = false) String code, HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_STRING);
        boolean hasPreference = false;
        if (accessToken == null) {
            accessToken = userService.getJwtToken(code);
        }  else {
            hasPreference = userService.existsPreference(accessToken);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        return new ResponseEntity<>(new CommonResponse<>("취향 선택 여부", hasPreference), headers, HttpStatus.OK);
    }

    @Operation(summary = "summary : 로그아웃",
            description = """
                    ## 요청 :
                    - header(Authorization Bearer *토큰* (필수))
                    ## 응답 :
                    - 없음
                    """)
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200")
    @PostMapping("/logout")
    ResponseEntity<Void> logout(HttpServletRequest request) {
        userService.logout(request.getHeader(HEADER_STRING));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}