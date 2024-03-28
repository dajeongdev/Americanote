package com.coffee.americanote.review.domain;

public record ReviewRequest(Long cafeId, Integer star, String contents) {
}
