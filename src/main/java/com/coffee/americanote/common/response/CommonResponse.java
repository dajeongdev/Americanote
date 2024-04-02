package com.coffee.americanote.common.response;

public record CommonResponse<T>(String message, T data) {}
