package com.coffee.americanote.user.repository.querydsl;

import com.coffee.americanote.user.domain.entity.UserToken;

import java.util.Optional;

public interface UserQueryRepository {

    Optional<UserToken> getUserTokenByKakaoId(Long kakaoId);
}