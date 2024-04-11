package com.coffee.americanote.coffee.domain.response;

import com.coffee.americanote.coffee.domain.entity.Coffee;
import com.coffee.americanote.coffee.domain.entity.CoffeeFlavour;
import com.coffee.americanote.common.entity.Degree;
import java.util.List;

public record CoffeeResponse(
        Long cafeId,
        String name,
        List<String> flavours,
        String intensity,
        String acidity,
        Integer price
) {
    public CoffeeResponse(Coffee coffee, List<CoffeeFlavour> flavours) {
        this(
                coffee.getCafe().getId(),
                coffee.getName(),
                flavours.stream().map(coffeeFlavour -> coffeeFlavour.getFlavour().getLabel()).toList(),
                coffee.getIntensity().getLabel(),
                coffee.getAcidity().getLabel(),
                coffee.getPrice()
        );
    }

    public CoffeeResponse(Long cafeId, String coffeeName, List<String> flavours, Degree intensity, Degree acidity,
                          Integer price) {
        this(
                cafeId,
                coffeeName,
                flavours,
                intensity.getLabel(),
                acidity.getLabel(),
                price
        );
    }
}
