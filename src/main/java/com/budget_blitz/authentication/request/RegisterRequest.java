package com.budget_blitz.authentication.request;

import com.budget_blitz.validation.email.NonDisposableEmail;
import com.budget_blitz.validation.password.PasswordMatches;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PasswordMatches
@Schema(
        name = "RegisterRequest",
        description = "Request object for register new user"
)
public class RegisterRequest {

    @NotBlank(message = "First name required")
    @Size(
            min = 2,
            max = 50,
            message = "First name must be between 2 to 50 character length"
    )
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only letters")
    @Schema(
            description = "User's first name must be between 2 and 50 characters.",
            example = "Osama"
    )
    private String firstName;

    @NotBlank(message = "Last name required")
    @Size(
            min = 2,
            max = 50,
            message = "Last name must be between 2 to 50 character length"
    )
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must contain only letters")
    @Schema(
            description = "User's last name must be between 2 and 50 characters.",
            example = "Salih"
    )
    private String lastName;

    @NotBlank(message = "Email required")
    @Email(message = "Invalid email format")
    @NonDisposableEmail
    @Schema(description = "User's email address", example = "osama@budget.blitz.com")
    private String email;

    @NotNull(message = "Date of birth required")
    @Past(message = "Date of birth must be in the past")
    @Schema(description = "User's date of birth in YYYY-MM-DD format", example = "2000-06-06")
    private LocalDate dateOfBirth;

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

    @NotBlank(message = "Confirm password required")
    @Schema(example = "New#Strong1", description = "Confirmation of the new password")
    private String confirmPassword;
}
