package com.coffee.americanote.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommonValidationException extends RuntimeException {

    private final String message;
}
