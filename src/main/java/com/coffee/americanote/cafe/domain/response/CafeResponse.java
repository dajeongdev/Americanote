package com.coffee.americanote.cafe.domain.response;

import com.coffee.americanote.cafe.domain.entity.Cafe;

public record CafeResponse(
        Long id,
        Double latitude,
        Double longitude
) {
    public CafeResponse(Cafe cafe) {
        this(
                cafe.getId(),
                cafe.getLatitude(),
                cafe.getLongitude()
        );
    }
}
