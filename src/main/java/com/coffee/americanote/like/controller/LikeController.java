package com.coffee.americanote.like.controller;

import com.coffee.americanote.like.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/like")
@RequiredArgsConstructor
@RestController
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{cafeId}")
    ResponseEntity<Void> toggleLike(@PathVariable("cafeId") Long cafeId, HttpServletRequest request) {
        likeService.toggleLike(cafeId, request);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
