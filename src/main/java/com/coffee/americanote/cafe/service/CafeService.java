package com.coffee.americanote.cafe.service;

import com.coffee.americanote.cafe.domain.entity.Cafe;
import com.coffee.americanote.cafe.domain.request.SearchCafeRequest;
import com.coffee.americanote.cafe.domain.response.CafeResponse;
import com.coffee.americanote.cafe.repository.CafeRepository;
import com.coffee.americanote.coffee.domain.entity.Coffee;
import com.coffee.americanote.coffee.service.CoffeeService;
import com.coffee.americanote.common.entity.ErrorCode;
import com.coffee.americanote.common.validator.CommonValidator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CafeService {

    private final CafeRepository cafeRepository;
    private final CoffeeService coffeeService;

    public List<CafeResponse> getAllCafe() {
        List<CafeResponse> allCafe = new ArrayList<>();
        for (Cafe cafe : cafeRepository.findAll()) {
            allCafe.add(new CafeResponse(cafe));
        }
        return allCafe;
    }

    public CafeResponse getCoordinate(Long cafeId) {
        Optional<Cafe> cafe = cafeRepository.findById(cafeId);
        CommonValidator.notNullOrThrow(cafe.orElse(null), ErrorCode.RESOURCE_NOT_FOUND.getErrorMessage());
        return new CafeResponse(cafe.get());
    }

    public Set<CafeResponse> searchCafe(SearchCafeRequest request) {
        List<Coffee> coffees = coffeeService.findAllBySearchOptions(request);

        Set<CafeResponse> result = new HashSet<>();
        for (Coffee coffee : coffees) {
            result.add(new CafeResponse(coffee.getCafe()));
        }
        return result;
    }
}
