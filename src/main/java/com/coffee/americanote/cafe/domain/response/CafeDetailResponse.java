package com.coffee.americanote.cafe.domain.response;

import com.coffee.americanote.coffee.domain.response.CoffeeResponse;
import com.coffee.americanote.common.entity.Degree;
import com.coffee.americanote.common.entity.Flavour;
import com.coffee.americanote.review.domain.response.ReviewResponse;
import java.util.Arrays;
import java.util.List;

public record CafeDetailResponse(
        Long id,
        Double latitude,
        Double longitude,
        String imageUrl,
        String cafeName,
        Double avgStar,
        CoffeeResponse coffeeDetail,
        List<ReviewResponse> reviews,
        Boolean hasLike
) {
    public CafeDetailResponse(Long cafeId, Double latitude, Double longitude, String imageUrl, String cafeName,
                              Double avgStar, String coffeeName, String flavours, Degree intensity, Degree acidity,
                              Integer price, Boolean hasLike) {
        this(
                cafeId,
                latitude,
                longitude,
                imageUrl,
                cafeName,
                avgStar,
                new CoffeeResponse(cafeId, coffeeName,
                        Arrays.stream(flavours.split(","))
                                .map(Flavour::valueOf).distinct().map(Flavour::getLabel).toList(),
                        intensity, acidity, price),
                null,
                hasLike
        );
    }
    public CafeDetailResponse(Long cafeId, Double latitude, Double longitude, String imageUrl, String cafeName,
                              Double avgStar, String coffeeName, String flavours, Degree intensity, Degree acidity,
                              Integer price, List<ReviewResponse> reviews, Boolean hasLike) {
        this(
                cafeId,
                latitude,
                longitude,
                imageUrl,
                cafeName,
                avgStar,
                new CoffeeResponse(cafeId, coffeeName,
                        Arrays.stream(flavours.split(","))
                                .map(Flavour::valueOf).distinct().map(Flavour::getLabel).toList(),
                        intensity, acidity, price),
                reviews,
                hasLike
        );
    }

}
