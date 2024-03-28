package com.coffee.americanote.coffee.repository;

import com.coffee.americanote.cafe.domain.request.SearchCafeRequest;
import com.coffee.americanote.coffee.domain.entity.CoffeeFlavour;
import java.util.List;

public interface CoffeeFlavourRepositoryCustom {
    List<CoffeeFlavour> findInFlavours(SearchCafeRequest request);
}
