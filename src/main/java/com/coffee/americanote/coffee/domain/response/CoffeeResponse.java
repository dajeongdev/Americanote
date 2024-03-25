package com.coffee.americanote.coffee.domain.response;

import com.coffee.americanote.coffee.domain.entity.Coffee;
import com.coffee.americanote.coffee.domain.entity.CoffeeFlavour;

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
}
