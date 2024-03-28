package com.coffee.americanote.common.response;

import com.coffee.americanote.common.entity.ErrorCode;
import org.springframework.http.ResponseEntity;

public record ErrorResponse(
        ErrorCode errorCode,
        String message
) {
    public static ResponseEntity<ErrorResponse> of(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus().value())
                .body(new ErrorResponse(errorCode, message));
    }
}
