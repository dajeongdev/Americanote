package com.coffee.americanote.cafe.controller;

import com.coffee.americanote.cafe.domain.request.SearchCafeRequest;
import com.coffee.americanote.cafe.domain.response.CafeDetailResponse;
import com.coffee.americanote.cafe.domain.response.CafePreviewResponse;
import com.coffee.americanote.cafe.domain.response.CafeResponse;
import com.coffee.americanote.cafe.domain.response.CafeSearchResponse;
import com.coffee.americanote.cafe.service.CafeService;
import com.coffee.americanote.common.response.BasicApiSwaggerResponse;
import com.coffee.americanote.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "Cafe", description = "카페 관련 API입니다.")
@RequestMapping("/api/cafe")
@RequiredArgsConstructor
@RestController
class CafeController {

    final String HEADER_STRING = "Authorization";
    private final CafeService cafeService;

    @Operation(summary = "summary : 모든 카페 조회",
            description = """
                    ## 요청 :
                    - 없음
                    ## 응답 :
                    - [카페 아이디, 좌표] 리스트
                    """)
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("/all")
    ResponseEntity<CommonResponse<List<CafeResponse>>> getAllCafes() {
        return new ResponseEntity<>(new CommonResponse<>("모든 카페 조회", cafeService.getAllCafes()), HttpStatus.OK);
    }

    @Operation(summary = "summary : 필터링 검색",
            description = """
                    ## 요청 :
                    - 가격 범위, 향, 강도, 산미
                    ## 응답 :
                    - [카페 아이디, 좌표] 리스트
                    """)
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @PostMapping("/filter")
    ResponseEntity<CommonResponse<Set<CafeResponse>>> searchCafeByFiltering(@RequestBody SearchCafeRequest request) {
        return new ResponseEntity<>(new CommonResponse<>("카페 필터링 검색", cafeService.searchCafeByFiltering(request)), HttpStatus.OK);
    }

    @Operation(summary = "summary : 카페 바텀시트",
            description = """
                    ## 요청 :
                    - 카페 아이디, header(Authorization Bearer *토큰* (옵션))
                    ## 응답 :
                    카페 정보, 평점, 커피 정보, 리뷰들, 좋아요 여부(boolean)
                    """)
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("/info")
    ResponseEntity<CommonResponse<CafeDetailResponse>> getCafeDetail(@RequestParam("id") Long id, HttpServletRequest request) {
        return new ResponseEntity<>(new CommonResponse<>("카페 정보 보기", cafeService.getCafeDetail(id, request.getHeader(HEADER_STRING))), HttpStatus.OK);
    }

    @Operation(summary = "summary : 취향에 맞는 카페",
            description = """
                    ## 요청 :
                    - header(Authorization Bearer *토큰* (필수!))
                    ## 응답 :
                    - [카페 정보, 평점, 커피 향/강도/산미, 좋아요 여부(boolean)] 리스트
                    """)
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("/recommend")
    ResponseEntity<CommonResponse<List<CafePreviewResponse>>> getRecommendCafes(HttpServletRequest request) {
        return new ResponseEntity<>(new CommonResponse<>("추천 카페 리스트", cafeService.getRecommendCafes(request.getHeader(HEADER_STRING))), HttpStatus.OK);
    }

    @Operation(summary = "summary : 카페 검색",
            description = """
                    ## 요청 :
                    - 검색어, header(Authorization Bearer *토큰* (옵션))
                    ## 응답 :
                    - [카페 정보, 커피 향/강도/산미, 평점, 좋아요 여부(boolean)] 리스트
                    """)
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("/search")
    ResponseEntity<CommonResponse<List<CafeSearchResponse>>> getAllSearchCafe(
            @RequestParam(value = "keyword") String keyword, HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_STRING);
        return new ResponseEntity<>(new CommonResponse<>(
                "카페 검색 목록 조회", cafeService.getAllSearchCafe(keyword, accessToken)), HttpStatus.OK);
    }

    @Operation(summary = "summary : 최근 검색어",
            description = """
                    ## 요청 :
                    - header(Authorization Bearer *토큰* (옵션))
                    ## 응답 :
                    - 검색어 리스트 (로그인 안했으면 빈 리스트 return)
                    """)
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("/recent")
    ResponseEntity<CommonResponse<List<String>>> getAllRecentSearchWord(HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_STRING);
        return new ResponseEntity<>(new CommonResponse<>(
                "최근 검색어 조회", cafeService.getAllRecentSearchWord(accessToken)), HttpStatus.OK);
    }

    @Operation(summary = "summary : 검색어 삭제",
            description = """
                    ## 요청 :
                    - 삭제할 검색어, header(Authorization Bearer *토큰* (필수!))
                    ## 응답 :
                    - ok
                    """)
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200")
    @DeleteMapping("/search")
    ResponseEntity<Void> deleteRecentSearchWord(
            @RequestParam(value = "keyword") String keyword, HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_STRING);
        cafeService.deleteRecentSearchWord(keyword, accessToken);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
