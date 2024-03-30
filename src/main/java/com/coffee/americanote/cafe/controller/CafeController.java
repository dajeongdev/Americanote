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
public class CafeController {

    public static final String HEADER_STRING = "Authorization";
    private final CafeService cafeService;

    @Operation(summary = "summary : 모든 카페 조회", description = "description : return all cafe list")
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("/all")
    ResponseEntity<CommonResponse<List<CafeResponse>>> getAllCafe() {
        return new ResponseEntity<>(new CommonResponse<>("모든 카페 조회", cafeService.getAllCafe()), HttpStatus.OK);
    }

    @Operation(summary = "summary : 필터링 검색", description = "description : return cafes coordinate")
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("/filter")
    ResponseEntity<CommonResponse<Set<CafeResponse>>> searchCafes(@RequestBody SearchCafeRequest request) {
        return new ResponseEntity<>(new CommonResponse<>("카페 필터링 검색", cafeService.searchCafeByFiltering(request)), HttpStatus.OK);
    }

    @Operation(summary = "summary : 카페 바텀시트", description = "description : return cafe detail info / token required!")
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("/info")
    ResponseEntity<CommonResponse<CafeDetailResponse>> getCafeDetail(@RequestParam("id") Long id, HttpServletRequest request) {
        return new ResponseEntity<>(new CommonResponse<>("카페 정보 보기", cafeService.getCafeDetail(id, request.getHeader(HEADER_STRING))), HttpStatus.OK);
    }

    @Operation(summary = "summary : 취향에 맞는 카페", description = "description : return cafe preview info / token required!")
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("/recommend")
    ResponseEntity<CommonResponse<List<CafePreviewResponse>>> getRecommendCafes(HttpServletRequest request) {
        return new ResponseEntity<>(new CommonResponse<>("추천 카페 리스트", cafeService.recommendCafes(request.getHeader(HEADER_STRING))), HttpStatus.OK);
    }

    @Operation(summary = "summary : 카페 검색", description = "description : return searching cafe list / token required!")
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("/search")
    ResponseEntity<CommonResponse<List<CafeSearchResponse>>> getAllSearchCafe(
            @RequestParam(value = "keyword") String keyword, HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_STRING);
        return new ResponseEntity<>(new CommonResponse<>(
                "카페 검색 목록 조회", cafeService.getAllSearchCafe(keyword, accessToken)), HttpStatus.OK);
    }

    @Operation(summary = "summary : 최근 검색어", description = "description : return recent search words / token required!")
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("/recent")
    ResponseEntity<CommonResponse<List<String>>> getAllRecentSearchWord(HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_STRING);
        return new ResponseEntity<>(new CommonResponse<>(
                "최근 검색어 조회", cafeService.getAllRecentSearchWord(accessToken)), HttpStatus.OK);
    }
}
