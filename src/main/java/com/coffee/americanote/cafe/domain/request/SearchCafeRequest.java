package com.coffee.americanote.cafe.domain.request;

import java.util.List;

public record SearchCafeRequest(
        String priceRange,
        List<String> flavours,
        List<String> intensities,
        List<String> acidities
) {
}
