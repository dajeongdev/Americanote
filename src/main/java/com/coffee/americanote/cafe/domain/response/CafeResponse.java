package com.coffee.americanote.cafe.domain.response;

public record CafeResponse(
        Long id,
        Double latitude,
        Double longitude
) {

    public CafeResponse(Long id, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
