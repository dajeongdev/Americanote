package com.coffee.americanote.review.domain.response;

import com.coffee.americanote.review.domain.entity.Review;

public record ReviewResponse(
        String nickName,
        String profileUrl,
        Integer star,
        String content
) {
    public ReviewResponse(Review review) {
        this(
                review.getUser().getNickname(),
                review.getUser().getProfileImageUrl(),
                review.getStar(),
                review.getContent()
        );
    }
}
