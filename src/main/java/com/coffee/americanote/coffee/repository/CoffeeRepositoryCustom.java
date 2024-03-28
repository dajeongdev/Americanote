package com.coffee.americanote.coffee.repository;

import com.coffee.americanote.cafe.domain.request.SearchCafeRequest;
import com.coffee.americanote.coffee.domain.entity.Coffee;
import java.util.List;

public interface CoffeeRepositoryCustom {
    List<Coffee> findBySearchOption(SearchCafeRequest request);
}
