package com.coffee.americanote.user.domain.request;

import com.coffee.americanote.common.entity.UserRole;
import lombok.Builder;

@Builder
public record KakaoLoginRequest(
        Long kakaoId,
        String nickname,
        String profileImageUrl,
        UserRole role
) {
    public KakaoLoginRequest {
        role = UserRole.ROLE_USER;
    }
}
