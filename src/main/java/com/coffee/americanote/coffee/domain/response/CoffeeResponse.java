package com.coffee.americanote.coffee.domain.response;

import com.coffee.americanote.coffee.domain.entity.Coffee;

public record CoffeeResponse(
        Long id,
        Long cafeId,
        String name,
        String intensity,
        String acidity,
        int price
) {
    public CoffeeResponse(Coffee coffee) {
        this(
                coffee.getId(),
                coffee.getCafe().getId(),
                coffee.getName(),
                coffee.getIntensity().getLabel(),
                coffee.getAcidity().getLabel(),
                coffee.getPrice()
        );
    }
}
