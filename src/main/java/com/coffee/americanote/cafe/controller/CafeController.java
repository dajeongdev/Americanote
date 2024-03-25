package com.coffee.americanote.cafe.controller;

import com.coffee.americanote.cafe.domain.response.CafeResponse;
import com.coffee.americanote.cafe.service.CafeService;
import com.coffee.americanote.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cafe", description = "카페 관련 API입니다.")
@RequiredArgsConstructor
@RestController
public class CafeController {

    private final CafeService cafeService;

    @GetMapping("/get/all/cafe")
    ResponseEntity<CommonResponse<List<CafeResponse>>> getAllCafe() {
        return new ResponseEntity<>(new CommonResponse<>("모든 카페 조회", cafeService.getAllCafe()), HttpStatus.OK);
    }
}
