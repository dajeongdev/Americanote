package com.coffee.americanote.user.domain.request;

import java.util.List;

public record UserPreferRequest(
        List<String> flavours,
        String intensity,
        String acidity
) {
}
