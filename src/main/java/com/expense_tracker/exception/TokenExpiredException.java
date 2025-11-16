package com.expense_tracker.exception;

import org.springframework.http.HttpStatus;

public class TokenExpiredException extends ApiException {
    public TokenExpiredException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}