package com.coffee.americanote.common.exception;

import com.coffee.americanote.common.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(CommonException.class)
    protected ResponseEntity<CommonResponse<?>> returnCommonError(CommonException exception) {
        return new ResponseEntity<>(new CommonResponse<>(exception.getMessage(), null), exception.getStatus());
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<CommonResponse<?>> returnNotFoundException(NoSuchElementException exception) {
        return new ResponseEntity<>(
                new CommonResponse<>("resource not found : [ " + exception.getMessage() + "]", null),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonResponse<?>> returnServerError(CommonException exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(new CommonResponse<>(exception.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommonValidationException.class)
    protected ResponseEntity<CommonResponse<?>> returnValidationError(CommonValidationException exception) {
        return new ResponseEntity<>(
                new CommonResponse<>(exception.getMessage() + " 정보를 확인해주세요.", null),
                HttpStatus.BAD_REQUEST);
    }
}