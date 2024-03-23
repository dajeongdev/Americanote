package com.coffee.americanote.user.controller;

import com.coffee.americanote.user.User;
import com.coffee.americanote.user.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관련 API입니다.")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/cafe")
    ResponseEntity<Void> findAllCafe() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/save")
    ResponseEntity<User> saveUser(@RequestBody User user) {
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
    }
}
