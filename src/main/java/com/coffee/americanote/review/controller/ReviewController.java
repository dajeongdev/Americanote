package com.coffee.americanote.review.controller;

import com.coffee.americanote.review.domain.ReviewRequest;
import com.coffee.americanote.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/api/review")
@RequiredArgsConstructor
@RestController
class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/save") // save dummy Data
    ResponseEntity<Void> saveReview(@RequestBody List<ReviewRequest> reviewRequests) {
        reviewService.saveReview(reviewRequests);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
