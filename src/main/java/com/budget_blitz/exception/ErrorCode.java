package com.budget_blitz.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("USER_NOT_FOUND", "User not found with id %s", HttpStatus.NOT_FOUND),
    INVALID_CURRENT_PASSWORD("INVALID_CURRENT_PASSWORD", "Invalid current password", HttpStatus.BAD_REQUEST),
    ACCOUNT_ALREADY_DEACTIVATE("ACCOUNT_ALREADY_DEACTIVATE","Your account is already deactivate", HttpStatus.BAD_REQUEST),
    ACCOUNT_ALREADY_ACTIVATE("ACCOUNT_ALREADY_ACTIVATE","Your account is already active", HttpStatus.BAD_REQUEST),
    INVALID_JWT_TOKEN("INVALID_JWT_TOKEN", "Invalid or malformed JWT token", HttpStatus.UNAUTHORIZED),
    INVALID_JWT_TOKEN_TYPE("INVALID_JWT_TOKEN_TYPE", "Invalid jwt token type", HttpStatus.UNAUTHORIZED),
    EXPIRED_JWT_TOKEN("EXPIRED_JWT_TOKEN", "Expired jwt token", HttpStatus.UNAUTHORIZED),
    INVALID_JWT_SIGNATURE("INVALID_JWT_SIGNATURE", "Invalid or tampered JWT signature", HttpStatus.UNAUTHORIZED),
    EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS", "Email already exists", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "Invalid credentials", HttpStatus.UNAUTHORIZED),
    EXPIRED_ACTIVATION_CODE("EXPIRED_ACTIVATION_CODE", "Activation code has expired new code has been sent to your email address", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_VERIFIED("EMAIL_ALREADY_VERIFIED", "Your email already has been verified", HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND("ENTITY_NOT_FOUND", "entity not found", HttpStatus.NOT_FOUND),
    USERNAME_NOT_FOUND("USERNAME_NOT_FOUND", "username not found", HttpStatus.NOT_FOUND),
    CONSTRAINT_VIOLATION("CONSTRAINT_VIOLATION", "Constraint violation error", HttpStatus.BAD_REQUEST),
    DISABLED_USER("DISABLED_USER", "User is disabled", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("ACCESS_DENIED", "Insufficient permissions" ,HttpStatus.UNAUTHORIZED),
    DATA_INTEGRITY_VIOLATION("DATA_INTEGRITY_VIOLATION", "Data integrity violation", HttpStatus.BAD_REQUEST),
    EMAIL_SENDING_FAILED("EMAIL_SENDING_FAILED", "Failed to send email. Please try again later", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_SERVER_ERR("INTERNAL_SERVER_ERR", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    HTTP_MESSAGE_NOT_READABLE("HTTP_MESSAGE_NOT_READABLE", "Malformed JSON request", HttpStatus.BAD_REQUEST),
    CATEGORY_ALREADY_EXISTS("CATEGORY_ALREADY_EXISTS", "You already have a category with the same name", HttpStatus.BAD_REQUEST),
    EXPENSE_ALREADY_EXISTS("EXPENSE_ALREADY_EXISTS", "You already hava an expense with the same amount and date for this category",HttpStatus.BAD_REQUEST),
    INCOME_ALREADY_EXISTS("INCOME_ALREADY_EXISTS", "You already hava an income with the same amount and date", HttpStatus.BAD_REQUEST),
    USER_EMAIL_NOT_FOUND("USER_EMAIL_NOT_FOUND", "No user with this email, Please double-check your email and try again.", HttpStatus.NOT_FOUND),
    RESET_CODE_ALREADY_VERIFIED("RESET_CODE_ALREADY_VERIFIED", "Reset code already has been verified", HttpStatus.BAD_REQUEST),
    EXPIRED_RESET_CODE("EXPIRED_RESET_CODE", "Reset code has expired new code has been sent to your email address", HttpStatus.BAD_REQUEST),

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
