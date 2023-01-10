package com.marketboro.reserve.exception;

public class InvalidRequestException extends RuntimeException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequestException(String message) {
        super(MESSAGE);
    }

    public int statusCode() {
        return 400;
    }
}
