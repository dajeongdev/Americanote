package com.coffee.americanote.coffee.domain.response;

import com.coffee.americanote.coffee.domain.entity.Coffee;
import com.coffee.americanote.coffee.domain.entity.CoffeeFlavour;
import java.util.List;

public record CoffeeResponse(
        String name,
        List<CoffeeFlavourResponse> flavours,
        String intensity,
        String acidity,
        Integer price
) {
    public CoffeeResponse(Coffee coffee, List<CoffeeFlavour> flavours) {
        this(
                coffee.getName(),
                flavours.stream().map(CoffeeFlavourResponse::new)
                                .toList(),
                coffee.getIntensity().getLabel(),
                coffee.getAcidity().getLabel(),
                coffee.getPrice()
        );
    }

    public record CoffeeFlavourResponse(
            String flavour
    ){
        public CoffeeFlavourResponse(CoffeeFlavour coffeeFlavour) {
            this(
                    coffeeFlavour.getFlavour().getLabel()
            );
        }
    }
}
