package com.coffee.americanote.common.validator;

import com.coffee.americanote.common.exception.CommonValidationException;
import jakarta.annotation.Nullable;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

@NoArgsConstructor
public class CommonValidator {

    public static void isTrueOrThrow(boolean expression, String message) {
        if (!expression) {
            throw new CommonValidationException(message);
        }
    }

    public static void isValidOrThrow(Number number, String message) {
        if (number == null || number.doubleValue() <= 0) {
            throw new CommonValidationException(message);
        }
    }

    public static void isNullOrThrow(@Nullable Object object, String message) {
        if (object != null) {
            throw new CommonValidationException(message);
        }
    }

    public static void notNullOrThrow(@Nullable Object object, String message) {
        if (object == null) {
            throw new CommonValidationException(message);
        }
    }

    public static void hasTextOrThrow(@Nullable String text, String message) {
        if (!StringUtils.hasText(text)) {
            throw new CommonValidationException(message);
        }
    }

    public static void notEmptyList(List<?> list, String message) {
        if (list.isEmpty()) {
            throw new CommonValidationException(message);
        }
    }

    public static void hasNoEmptyElementOrThrow(List<?> list, String message) {
        list.forEach(e -> notNullOrThrow(e, message));
    }
}
