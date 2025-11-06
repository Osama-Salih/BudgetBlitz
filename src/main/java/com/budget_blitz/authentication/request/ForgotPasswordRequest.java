package com.budget_blitz.authentication.request;
import com.budget_blitz.validation.email.NonDisposableEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Request to initiate password reset via email")
public class ForgotPasswordRequest {

    @NotBlank(message = "Email required")
    @Email(message = "Invalid email format")
    @NonDisposableEmail
    @Schema(description = "User's email address", example = "osama@budget.blitz.com")
    private String email;
}
