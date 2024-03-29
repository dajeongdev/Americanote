package com.coffee.americanote.coffee.controller;

import com.coffee.americanote.coffee.domain.response.CoffeeResponse;
import com.coffee.americanote.coffee.service.CoffeeService;
import com.coffee.americanote.common.response.CommonResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/coffee")
@RequiredArgsConstructor
@RestController
public class CoffeeController {

    private final CoffeeService coffeeService;

    // test api
    @GetMapping("/all")
    ResponseEntity<CommonResponse<List<CoffeeResponse>>> getAllCoffee() {
        return new ResponseEntity<>(new CommonResponse<>("모든 커피 조회", coffeeService.getAllCoffeeData()), HttpStatus.OK);
    }
}
