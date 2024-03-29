package com.coffee.americanote.cafe.repository.querydsl;

import com.coffee.americanote.mypage.domain.response.UserLikeCafeResponse;

import java.util.List;

public interface CafeQueryRepository {

    List<UserLikeCafeResponse> getAllUserLikeCafe(Long userId);
}
