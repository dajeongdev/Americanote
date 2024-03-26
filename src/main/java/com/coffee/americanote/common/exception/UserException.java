package com.coffee.americanote.common.exception;

import com.coffee.americanote.common.entity.ErrorCode;

public class UserException extends CustomException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
