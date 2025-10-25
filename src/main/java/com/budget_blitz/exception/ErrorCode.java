package com.budget_blitz.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("USER_NOT_FOUND", "User not found with id %s", HttpStatus.NOT_FOUND),
    INVALID_CURRENT_PASSWORD("INVALID_CURRENT_PASSWORD", "Invalid current password", HttpStatus.BAD_REQUEST),
    ACCOUNT_ALREADY_DEACTIVATE("ACCOUNT_ALREADY_DEACTIVATE","Your account is already deactivate", HttpStatus.BAD_REQUEST),
    ACCOUNT_ALREADY_ACTIVATE("ACCOUNT_ALREADY_ACTIVATE","Your account is already active", HttpStatus.BAD_REQUEST),

    ;
    private final String code;
    private final String defaultMessage;
    private final HttpStatus status;

    ErrorCode(String code, String defaultMessage, HttpStatus status) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.status = status;
    }
}
