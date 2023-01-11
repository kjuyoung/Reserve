package com.marketboro.reserve.exception;

public class InvalidRequestException extends RuntimeException {

    private static final String MESSAGE = "Bad request";

    public InvalidRequestException(String message) {
        super(MESSAGE);
    }
}
