package com.coffee.americanote.cafe.controller;

import com.coffee.americanote.cafe.domain.response.CafeResponse;
import com.coffee.americanote.cafe.service.CafeService;
import com.coffee.americanote.common.response.BasicApiSwaggerResponse;
import com.coffee.americanote.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Cafe", description = "카페 관련 API입니다.")
@RequestMapping("/api/cafe")
@RequiredArgsConstructor
@RestController
public class CafeController {

    private final CafeService cafeService;

    @Operation(summary = "summary : 모든 카페 조회", description = "description : return all cafe list")
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("/all")
    ResponseEntity<CommonResponse<List<CafeResponse>>> getAllCafe() {
        return new ResponseEntity<>(new CommonResponse<>("모든 카페 조회", cafeService.getAllCafe()), HttpStatus.OK);
    }
}
