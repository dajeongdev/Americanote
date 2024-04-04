package com.coffee.americanote.cafe.repository.querydsl;

import com.coffee.americanote.cafe.domain.request.SearchCafeRequest;
import com.coffee.americanote.cafe.domain.response.CafeDetailResponse;
import com.coffee.americanote.cafe.domain.response.CafeResponse;
import com.coffee.americanote.cafe.domain.response.CafeSearchResponse;

import java.util.List;
import java.util.Set;

public interface CafeQueryRepository {

    List<CafeSearchResponse> getAllUserLikeCafe(Long userId);

    List<CafeSearchResponse> getAllSearchCafe(String keyword, Long userId);

    Set<CafeResponse> getAllFilteringCafe(SearchCafeRequest request);

    CafeDetailResponse getCafeDetail(Long cafeId, Long userId);
}
