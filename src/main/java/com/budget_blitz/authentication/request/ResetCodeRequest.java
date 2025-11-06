package com.budget_blitz.authentication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Request payload containing the verification code sent to the user's email for password reset.")
public class ResetCodeRequest {

    @NotBlank(message = "Reset code required")
    @Size(
            min = 6,
            max = 6,
            message = "Reset code must be 6 digits"
    )
    @Positive(message = "Reset code must be positive")
    @Schema(
            description = "6-digit verification code sent to user's email",
            example = "123456"
    )
    private String resetCode;
}
