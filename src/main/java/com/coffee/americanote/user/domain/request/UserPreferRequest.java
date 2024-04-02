package com.coffee.americanote.user.domain.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserPreferRequest(
        @NotNull(message = "향은 필수값입니다.")
        List<String> flavours,
        @NotNull(message = "강도는 필수값입니다.")
        String intensity,
        @NotNull(message = "산미는 필수값입니다.")
        String acidity
) {
}
