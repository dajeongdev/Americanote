package com.coffee.americanote.coffee.domain.response;

import com.coffee.americanote.coffee.domain.entity.Coffee;
import com.coffee.americanote.coffee.domain.entity.CoffeeFlavour;
import java.util.List;
import java.util.stream.Collectors;

public record CoffeeResponse(
        Long id,
        Long cafeId,
        String name,
        List<CoffeeFlavourResponse> flavours,
        String intensity,
        String acidity,
        Integer price
) {
    public CoffeeResponse(Coffee coffee, List<CoffeeFlavour> flavours) {
        this(
                coffee.getId(),
                coffee.getCafe().getId(),
                coffee.getName(),
                flavours.stream().map(CoffeeFlavourResponse::new)
                                .collect(Collectors.toList()),
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
