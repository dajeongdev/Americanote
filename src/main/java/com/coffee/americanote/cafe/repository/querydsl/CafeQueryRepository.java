package com.coffee.americanote.cafe.repository.querydsl;

import com.coffee.americanote.cafe.domain.response.CafeSearchResponse;

import java.util.List;

public interface CafeQueryRepository {

    List<CafeSearchResponse> getAllUserLikeCafe(Long userId);

    List<CafeSearchResponse> getAllSearchCafe(String keyword, Long userId);
}
