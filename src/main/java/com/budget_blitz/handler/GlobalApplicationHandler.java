package com.budget_blitz.handler;

import com.budget_blitz.exception.BusinessException;
import com.budget_blitz.exception.ErrorCode;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailSendException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalApplicationHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleException(final BusinessException ex) {
        final ErrorResponse body = ErrorResponse.builder()
                .code(ex.getErrorCode().getCode())
                .defaultMessage(ex.getMessage())
                .build();

        log.info("Business exception {} ", ex.getMessage());
        log.debug(ex.getMessage(), ex);

        return ResponseEntity.status(ex.getErrorCode().getStatus() != null
                        ? ex.getErrorCode().getStatus() : HttpStatus.BAD_REQUEST)
                .body(body);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(final UsernameNotFoundException ex) {
        log.debug(ex.getMessage(), ex);
        final ErrorResponse body = ErrorResponse
                .builder()
                .code(ErrorCode.USERNAME_NOT_FOUND.getCode())
                .defaultMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(ErrorCode.USERNAME_NOT_FOUND.getStatus()).body(body);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(final EntityNotFoundException ex) {
          log.debug(ex.getMessage(), ex);
          final ErrorResponse body = ErrorResponse
                  .builder()
                  .code(ErrorCode.ENTITY_NOT_FOUND.getCode())
                  .defaultMessage(ex.getMessage())
                  .build();

          return ResponseEntity.status(ErrorCode.ENTITY_NOT_FOUND.getStatus()).body(body);
      }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(final MethodArgumentNotValidException ex) {
        final List<ErrorResponse.ValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> ErrorResponse.ValidationError.builder()
                        .field(fieldError.getField())
                        .code(fieldError.getCode())
                        .message(fieldError.getDefaultMessage())
                        .build()).toList();

        final ErrorResponse response = ErrorResponse.builder()
                .validationErrors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleException(final BindException ex) {
        final List<ErrorResponse.ValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> ErrorResponse.ValidationError.builder()
                        .field(fieldError.getField())
                        .code(fieldError.getCode())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .toList();

        final ErrorResponse response = ErrorResponse.builder()
                .validationErrors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleException(final ConstraintViolationException ex) {
        log.debug(ex.getMessage(), ex);
        final ErrorResponse body = ErrorResponse
                .builder()
                .code(ErrorCode.CONSTRAINT_VIOLATION.getCode())
                .defaultMessage(ex.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.CONSTRAINT_VIOLATION.getStatus()).body(body);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(final BadCredentialsException ex) {
        final ErrorResponse body = ErrorResponse.builder()
                .code(ErrorCode.INVALID_CREDENTIALS.getCode())
                .defaultMessage(ErrorCode.INVALID_CREDENTIALS.getDefaultMessage())
                .build();
        return ResponseEntity.status(ErrorCode.INVALID_CREDENTIALS.getStatus()).body(body);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleException(final DisabledException ex) {
        final ErrorResponse body = ErrorResponse.builder()
                .code(ErrorCode.DISABLED_USER.getCode())
                .defaultMessage(ex.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.DISABLED_USER.getStatus()).body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleException(final AccessDeniedException ex) {
        final ErrorResponse body = ErrorResponse.builder()
                .code(ErrorCode.ACCESS_DENIED.getCode())
                .defaultMessage(ex.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.ACCESS_DENIED.getStatus()).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleException(final DataIntegrityViolationException ex) {
          final ErrorResponse body = ErrorResponse.builder()
                  .code(ErrorCode.DATA_INTEGRITY_VIOLATION.getCode())
                  .defaultMessage(ex.getMessage())
                  .build();
          return ResponseEntity.status(ErrorCode.DATA_INTEGRITY_VIOLATION.getStatus()).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleException(final HttpMessageNotReadableException ex) {

        final Throwable cause = ex.getCause();
        final String errorMessage;
        if (cause instanceof InvalidFormatException invalidFormatException &&
            invalidFormatException.getTargetType() == LocalDate.class) {
            errorMessage = "Invalid date format. Expected format: yyyy-MM-dd";
        } else {
            errorMessage = ErrorCode.HTTP_MESSAGE_NOT_READABLE.getDefaultMessage();
        }

        final ErrorResponse body = ErrorResponse.builder()
                  .code(ErrorCode.HTTP_MESSAGE_NOT_READABLE.getCode())
                  .defaultMessage(errorMessage)
                  .build();
          return ResponseEntity.status(ErrorCode.HTTP_MESSAGE_NOT_READABLE.getStatus()).body(body);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleException(final MissingServletRequestParameterException ex) {
          final ErrorResponse body = ErrorResponse.builder()
                  .code("MissingServletRequestParameterException")
                  .defaultMessage(ex.getMessage())
                  .build();
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleException(final MethodArgumentTypeMismatchException ex) {
        final ErrorResponse body = ErrorResponse.builder()
                .code("MethodArgumentTypeMismatchException")
                .defaultMessage(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleException(final HttpRequestMethodNotSupportedException ex) {
        final ErrorResponse body = ErrorResponse.builder()
                .code("HttpRequestMethodNotSupportedException")
                .defaultMessage(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleException(final SignatureException ex) {
        final ErrorResponse body = ErrorResponse.builder()
                .code(ErrorCode.INVALID_JWT_SIGNATURE.getCode())
                .defaultMessage(ErrorCode.INVALID_JWT_SIGNATURE.getDefaultMessage())
                .build();
        return ResponseEntity.status(ErrorCode.INVALID_JWT_SIGNATURE.getStatus()).body(body);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleException(final ExpiredJwtException ex) {
        final ErrorResponse body = ErrorResponse.builder()
                .code(ErrorCode.EXPIRED_JWT_TOKEN.getCode())
                .defaultMessage(ErrorCode.EXPIRED_JWT_TOKEN.getDefaultMessage())
                .build();
        return ResponseEntity.status(ErrorCode.EXPIRED_JWT_TOKEN.getStatus()).body(body);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleException(final MissingRequestHeaderException ex) {
        final ErrorResponse body = ErrorResponse.builder()
                .code("MISSING_HEADER")
                .defaultMessage(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleException(final AuthenticationException ex) {
        final ErrorResponse body = ErrorResponse.builder()
                .code("AuthenticationException")
                .defaultMessage("Authentication failed: " + ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity <ErrorResponse> handleException(final MessagingException ex) {
        log.error("Email sending failed", ex);
        final ErrorResponse body = ErrorResponse.builder()
                .code(ErrorCode.EMAIL_SENDING_FAILED.getCode())
                .defaultMessage(ErrorCode.EMAIL_SENDING_FAILED.getDefaultMessage())
                .build();

        return ResponseEntity.status(ErrorCode.EMAIL_SENDING_FAILED.getStatus()).body(body);
    }

    @ExceptionHandler(MailSendException.class)
    public ResponseEntity <ErrorResponse> handleException(final MailSendException ex) {
        log.error("Mail server connection failed", ex);
        final ErrorResponse body = ErrorResponse.builder()
                .code("MAIL_SERVER_CONNECTION_FAILED")
                .defaultMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception ex) {
        ErrorResponse body = ErrorResponse.builder()
                .code(ErrorCode.INTERNAL_SERVER_ERR.getCode())
                .defaultMessage(ex.getMessage())
                .build();

        log.error("Exception error occurred", ex);
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERR.getStatus()).body(body);
    }
}