package com.coffee.americanote.coffee.domain.response;

import com.coffee.americanote.coffee.domain.entity.CoffeeFlavour;

public record CoffeeFlavourResponse(
        Long id,
        Long coffeeId,
        String flavour
){
    public CoffeeFlavourResponse(CoffeeFlavour coffeeFlavour) {
        this(
                coffeeFlavour.getId(),
                coffeeFlavour.getCoffee().getId(),
                coffeeFlavour.getFlavour().getLabel()
        );
    }
}