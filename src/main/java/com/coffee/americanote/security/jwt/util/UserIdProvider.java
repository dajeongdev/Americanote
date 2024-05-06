package com.coffee.americanote.security.jwt.util;

import com.coffee.americanote.common.entity.ErrorCode;
import com.coffee.americanote.common.exception.CommonValidationException;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserIdProvider {

    public static Long getUserIdOrDefault() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return !userId.trim().equals("anonymousUser") ? Long.parseLong(userId) : 0L;
    }

    public static Long getUserIdOrThrow() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userId.trim().equals("anonymousUser")) {
            throw new CommonValidationException(ErrorCode.EMPTY_TOKEN.getErrorMessage());
        }
        return Long.parseLong(userId);
    }
}
