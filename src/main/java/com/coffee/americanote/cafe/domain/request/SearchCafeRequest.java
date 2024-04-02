package com.coffee.americanote.cafe.domain.request;

import java.util.List;

public record SearchCafeRequest(
        List<String> priceRanges,
        List<String> flavours,
        List<String> intensities,
        List<String> acidities
) {
}
