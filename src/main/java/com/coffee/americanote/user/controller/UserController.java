package com.coffee.americanote.user.controller;

import com.coffee.americanote.user.User;
import com.coffee.americanote.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
