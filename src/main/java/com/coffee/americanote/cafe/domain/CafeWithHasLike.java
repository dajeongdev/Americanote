package com.coffee.americanote.cafe.domain;

import com.coffee.americanote.cafe.domain.entity.Cafe;
import lombok.Getter;

@Getter
public class CafeWithHasLike {
    private final Cafe cafe;
    private final Boolean hasLike;

    public CafeWithHasLike(Cafe cafe, Boolean hasLike) {
        this.cafe = cafe;
        this.hasLike = hasLike;
    }
}
