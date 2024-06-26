package com.coffee.americanote.mypage.controller;

import com.coffee.americanote.cafe.domain.response.CafeSearchResponse;
import com.coffee.americanote.common.response.BasicApiSwaggerResponse;
import com.coffee.americanote.common.response.CommonResponse;
import com.coffee.americanote.mypage.service.MyPageService;
import com.coffee.americanote.user.domain.request.UserPreferRequest;
import com.coffee.americanote.user.domain.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Mypage", description = "마이페이지 관련 API입니다.")
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@RestController
class MypageController {

    final String HEADER_STRING = "Authorization";
    private final MyPageService myPageService;

    @Operation(summary = "summary: 마이페이지 조회",
            description = """
                    ## 요청 :
                    - header(Authorization Bearer *토큰* (필수))
                    ## 응답 :
                    - 사용자 정보
                    """)
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("")
    ResponseEntity<CommonResponse<UserResponse>> getMyData() {
        return new ResponseEntity<>(new CommonResponse<>("마이페이지", myPageService.getMyData()), HttpStatus.OK);
    }

    @Operation(summary = "summary: 내 취향 커피 고르기",
            description = """
                    ## 요청 :
                    - header(Authorization Bearer *토큰* (필수)), 취향 요청 목록
                    - 요청 에러 시 400 BAD REQUEST
                    ## 응답 :
                    - 없음
                    """)
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200")
    @PutMapping("/choose-prefer")
    ResponseEntity<Void> updatePrefer(@Valid @RequestBody UserPreferRequest userPreferRequest){
        myPageService.updatePrefer(userPreferRequest);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @Operation(summary = "summary: 좋아요 목록",
            description = """
                    ## 요청 :
                    - header(Authorization Bearer *토큰* (필수))
                    ## 응답 :
                    - 좋아요한 카페 목록
                    """)
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("/like")
    ResponseEntity<CommonResponse<List<CafeSearchResponse>>> getAllUserLikeCafe() {
        return new ResponseEntity<>(new CommonResponse<>("마이페이지 좋아요 목록 조회", myPageService.getAllUserLikeCafe()), HttpStatus.OK);
    }
}