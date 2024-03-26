package com.coffee.americanote.common.exception;

import com.coffee.americanote.common.entity.ErrorCode;
import lombok.Getter;

@Getter
public class TokenException extends CustomException {

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
