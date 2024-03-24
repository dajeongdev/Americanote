package com.coffee.americanote.user.domain.response;

import com.coffee.americanote.user.domain.entity.User;

public record UserResponse(
        Long id,
        String name
) {
    public UserResponse(User user) {
        this(
          user.getId(),
          user.getName()
        );
    }
}