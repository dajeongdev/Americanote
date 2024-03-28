package com.coffee.americanote.cafe.domain.response;

import com.coffee.americanote.coffee.domain.response.CoffeeResponse;
import java.util.List;

public record CafeDetailResponse(
        Long cafeId,
        String imageUrl,
        String cafeName,
        Double avgStar,
        CoffeeResponse coffeeDetail,
        //List<review> reviews,
        Boolean isHeart
) {
}
