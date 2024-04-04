package com.coffee.americanote.review.domain.response;

public record ReviewResponse(
        String nickName,
        String profileUrl,
        Integer star,
        String content
) {
    public ReviewResponse(String nickName, String profileUrl, Integer star, String content) {
        this.nickName = nickName;
        this.profileUrl = profileUrl;
        this.star = star;
        this.content = content;
    }
}
