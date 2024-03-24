package com.coffee.americanote.user.controller;

import com.coffee.americanote.user.domain.entity.User;
import com.coffee.americanote.user.domain.request.UserRequest;
import com.coffee.americanote.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "사용자 관련 API입니다.")
@RequiredArgsConstructor
@RestController
class UserController {

    private final UserService userService;

    @GetMapping("/cafe")
    ResponseEntity<Void> findAllCafe() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/save")
    ResponseEntity<User> saveUser(@RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.save(userRequest), HttpStatus.OK);
    }
}
