package com.coffee.americanote.cafe.domain.response;

import com.coffee.americanote.cafe.domain.entity.Cafe;
import com.coffee.americanote.coffee.domain.response.CoffeeResponse.CoffeeFlavourResponse;
import com.coffee.americanote.review.domain.entity.Review;
import java.util.List;

public record CafePreviewResponse(
        Long id,
        String imageUrl,
        String cafeName,
        Double avgStar,
        List<CoffeeFlavourResponse> flavours,
        String intensity,
        String acidity,
        Boolean isHeart
) {
    public CafePreviewResponse(Cafe cafe, List<Review> reviews, Boolean isHeart) {
        this(
                cafe.getId(),
                cafe.getImageUrl(),
                cafe.getName(),
                Math.round((reviews.isEmpty() ? 0.0 :
                        reviews.stream().mapToDouble(Review::getStar).average()
                                .orElse(0.0)) * 10.0) / 10.0,
                cafe.getCoffees().get(0).getFlavours()
                        .stream().map(CoffeeFlavourResponse::new).toList(),
                cafe.getCoffees().get(0).getIntensity().getLabel(),
                cafe.getCoffees().get(0).getAcidity().getLabel(),
                isHeart
                );
    }
}
