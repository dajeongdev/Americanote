package com.coffee.americanote.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CommonException extends RuntimeException {

    private final String message;
    private final HttpStatus status;
}
