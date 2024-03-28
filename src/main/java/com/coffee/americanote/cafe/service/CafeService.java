package com.coffee.americanote.cafe.service;

import com.coffee.americanote.cafe.domain.entity.Cafe;
import com.coffee.americanote.cafe.domain.response.CafeResponse;
import com.coffee.americanote.cafe.repository.CafeRepository;
import com.coffee.americanote.common.entity.ErrorCode;
import com.coffee.americanote.common.validator.CommonValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CafeService {

    private final CafeRepository cafeRepository;

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
}
