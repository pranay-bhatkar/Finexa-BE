package com.expense_tracker.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends ApiException {
    public InvalidTokenException(String message) {

        super(message, HttpStatus.UNAUTHORIZED);
    }
}