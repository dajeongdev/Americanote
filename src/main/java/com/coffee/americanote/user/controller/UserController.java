package com.coffee.americanote.user.controller;

import com.coffee.americanote.common.response.CommonResponse;
import com.coffee.americanote.user.domain.request.UserRequest;
import com.coffee.americanote.user.domain.response.UserResponse;
import com.coffee.americanote.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "User", description = "사용자 관련 API입니다.")
@RequiredArgsConstructor
@RestController
class UserController {

    private final UserService userService;

    @GetMapping()
    ResponseEntity<CommonResponse<List<UserResponse>>> getAllUser() {
        return new ResponseEntity<>(new CommonResponse<>("모든 사용자 조회", userService.getAllUser()), HttpStatus.OK);
    }

    @PostMapping("/save")
    void saveUser(@RequestBody UserRequest userRequest) {
        userService.save(userRequest);
    }
}