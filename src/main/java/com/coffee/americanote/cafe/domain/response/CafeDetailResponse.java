package com.coffee.americanote.cafe.domain.response;

import com.coffee.americanote.cafe.domain.entity.Cafe;
import com.coffee.americanote.coffee.domain.response.CoffeeResponse;
import com.coffee.americanote.review.domain.entity.Review;
import com.coffee.americanote.review.domain.response.ReviewResponse;
import java.util.List;

public record CafeDetailResponse(
        Long cafeId,
        String imageUrl,
        String cafeName,
        Double avgStar,
        CoffeeResponse coffeeDetail,
        List<ReviewResponse> reviews,
        Boolean isHeart
) {
    public CafeDetailResponse(Cafe cafe, List<Review> reviews, Boolean isHeart) {
        this(
                cafe.getId(),
                cafe.getImageUrl(),
                cafe.getName(),
                Math.round((reviews.isEmpty() ? 0.0 :
                        reviews.stream().mapToDouble(Review::getStar).average()
                                .orElse(0.0)) * 10.0) / 10.0,
                new CoffeeResponse(cafe.getCoffees().get(0), cafe.getCoffees().get(0).getFlavours()),
                reviews.stream().map(ReviewResponse::new).toList(),
                isHeart
        );
    }
}
