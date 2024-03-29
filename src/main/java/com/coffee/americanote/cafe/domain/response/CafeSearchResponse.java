package com.coffee.americanote.cafe.domain.response;

import com.coffee.americanote.coffee.domain.response.CoffeeFlavourDegreeResponse;
import com.coffee.americanote.common.entity.Degree;
import com.coffee.americanote.common.entity.Flavour;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class CafeSearchResponse {

    private final Long cafeId;
    private final String cafeName;
    private final String imageUrl;
    private final CoffeeFlavourDegreeResponse coffeeDetail;
    private final double avgStar;
    private final boolean hasLike;

    public CafeSearchResponse(Long cafeId, String cafeName, String imageUrl, Degree intensity, Degree acidity, String flavours, double avgStar, boolean hasLike) {

        this.cafeId = cafeId;
        this.cafeName = cafeName;
        this.imageUrl = imageUrl;
        this.coffeeDetail = new CoffeeFlavourDegreeResponse(intensity, acidity,
                Arrays.stream(flavours.split(",")).map(Flavour::valueOf).distinct().map(Flavour::getLabel).toList());
        this.avgStar = avgStar;
        this.hasLike = hasLike;
    }
}