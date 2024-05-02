package com.coffee.americanote.like.controller;

import com.coffee.americanote.common.response.BasicApiSwaggerResponse;
import com.coffee.americanote.like.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Like", description = "좋아요 관련 API입니다.")
@RequestMapping("/api/like")
@RequiredArgsConstructor
@RestController
class LikeController {

    private final LikeService likeService;

    @Operation(summary = "summary: 좋아요 등록/삭제",
            description = """
                    ## 요청 :
                    - header(Authorization Bearer *토큰* (필수)), cafeId(Long))
                    ## 응답 :
                    - 없음
                    """)
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200")
    @PostMapping("")
    ResponseEntity<Void> toggleLike(@RequestParam("cafeId") Long cafeId, HttpServletRequest request) {
        likeService.toggleLike(cafeId, request);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
