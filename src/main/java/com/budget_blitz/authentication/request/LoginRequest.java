package com.budget_blitz.authentication.request;

import com.budget_blitz.validation.email.NonDisposableEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(
        name = "LoginRequest",
        description = "Request object for login user"
)
public class LoginRequest {

    @NotBlank(message = "Email required")
    @Email(message = "Invalid email format")
    @NonDisposableEmail
    @Schema(description = "User's email address", example = "osama@budget.blitz.com")
    private String email;

    @NotBlank(message = "password required")
    @Size(
            min = 8,
            max = 72,
            message = "Password must be between 8 to 72 character length"
    )
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\W).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, and one special character."
    )
    @Schema(example = "New#Strong1", description = "The password the user wants to set")
    private String password;
}
