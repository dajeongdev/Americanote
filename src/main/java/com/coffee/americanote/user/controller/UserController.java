package com.coffee.americanote.user.controller;

import com.coffee.americanote.user.User;
import com.coffee.americanote.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/cafe")
    ResponseEntity<Void> findAllCafe() {
        System.out.println("find all cafe");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/save")
    ResponseEntity<User> saveUser(@RequestBody User user) {
        System.out.println("save user");
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
    }
}
