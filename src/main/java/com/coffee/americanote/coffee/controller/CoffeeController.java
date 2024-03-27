package com.coffee.americanote.coffee.controller;

import com.coffee.americanote.coffee.domain.response.CoffeeResponse;
import com.coffee.americanote.coffee.domain.response.CoffeeResponse.CoffeeFlavourResponse;
import com.coffee.americanote.coffee.service.CoffeeService;
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
import java.util.Map;

@Tag(name = "Coffee", description = "커피 관련 API입니다.")
@RequestMapping("/api/coffee")
@RequiredArgsConstructor
@RestController
public class CoffeeController {

    private final CoffeeService coffeeService;

    @Operation(summary = "summary : 모든 커피 조회", description = "description : return all coffee list")
    @BasicApiSwaggerResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @GetMapping("/all")
    ResponseEntity<CommonResponse<Map<CoffeeResponse, List<CoffeeFlavourResponse>>>> getAllCoffee() {
        return new ResponseEntity<>(new CommonResponse<>("모든 커피 조회", coffeeService.getAllCoffeeData()), HttpStatus.OK);
    }
}
