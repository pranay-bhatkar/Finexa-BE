package com.expense_tracker.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialException extends ApiException {
    public InvalidCredentialException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}