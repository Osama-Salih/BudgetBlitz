package com.budget_blitz.handler;

import lombok.*;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ErrorResponse {

    private String code;
    private String defaultMessage;
    private List<ValidationError> validationErrors;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class ValidationError {
        private String field;
        private String code;
        private String message;
    }
}
