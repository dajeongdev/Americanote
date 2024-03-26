package com.coffee.americanote.user.domain.request;

import lombok.Builder;

@Builder
public record KakaoLoginRequest(
        Long kakaoId,
        String nickname,
        String profileImageUrl
) {
}
