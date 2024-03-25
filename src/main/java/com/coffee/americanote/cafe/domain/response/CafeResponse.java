package com.coffee.americanote.cafe.domain.response;

import com.coffee.americanote.cafe.domain.entity.Cafe;

public record CafeResponse(
        Long id,
        String name,
        String address,
        double latitude,
        double longitude,
        String photo
) {
    public CafeResponse(Cafe cafe) {
        this(
                cafe.getId(),
                cafe.getName(),
                cafe.getAddress(),
                cafe.getLatitude(),
                cafe.getLongitude(),
                cafe.getPhoto()
        );
    }
}
