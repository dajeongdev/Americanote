package com.coffee.americanote.cafe.domain.response;

import com.coffee.americanote.cafe.domain.entity.Cafe;
import com.coffee.americanote.coffee.domain.entity.Coffee;
import com.coffee.americanote.review.domain.entity.Review;
import java.util.List;

public record CafePreviewResponse(
        Long id,
        String imageUrl,
        String cafeName,
        Double avgStar,
        List<String> flavours,
        String intensity,
        String acidity,
        Boolean hasLike
) {
    public CafePreviewResponse(Cafe cafe, Coffee coffee, List<Review> reviews, Boolean hasLike) {
        this(
                cafe.getId(),
                cafe.getImageUrl(),
                cafe.getName(),
                Math.round((reviews.isEmpty() ? 0.0 :
                        reviews.stream().mapToDouble(Review::getStar).average()
                                .orElse(0.0)) * 10.0) / 10.0,
                coffee.getFlavours()
                        .stream().map(coffeeFlavour -> coffeeFlavour.getFlavour().getLabel()).toList(),
                coffee.getIntensity().getLabel(),
                coffee.getAcidity().getLabel(),
                hasLike
                );
    }
}
