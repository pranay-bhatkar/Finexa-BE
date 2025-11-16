package com.expense_tracker.exception;

import org.springframework.http.HttpStatus;

public class AccountLockedException extends ApiException {
    public AccountLockedException(String message) {
        super(message, HttpStatus.LOCKED);
    }
}