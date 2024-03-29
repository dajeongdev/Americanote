package com.coffee.americanote.coffee.domain.response;

import com.coffee.americanote.common.entity.Degree;

import java.util.List;

public record CoffeeFlavourDegreeResponse(
        String intensity,
        String acidity,
        List<String> flavours
) {
    public CoffeeFlavourDegreeResponse(Degree intensity, Degree acidity, List<String> flavours) {
        this (
                intensity.getLabel(),
                acidity.getLabel(),
                flavours
        );
    }
}
