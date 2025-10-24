package com.budget_blitz.users.request;

import com.budget_blitz.validation.password.PasswordMatches;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PasswordMatches
@Schema(description = "Request object for changing the user's password")
public class ChangePasswordRequest {

    @NotBlank(message = "Current password required")
    @Schema(example = "Old#Password1", description = "The user's current password")
    private String currentPassword;

    @NotBlank(message = "New password required")
    @Size(
            min = 8,
            max = 72,
            message = "New password must be between 8 to 72 character length"
    )
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\W).*$",
            message = "New password must contain at least one uppercase letter, one lowercase letter, and one special character."
    )
    @Schema(example = "New#Strong1", description = "The new password the user wants to set")
    private String newPassword;

    @NotBlank(message = "Confirm password required")
    @Schema(example = "New#Strong1", description = "Confirmation of the new password")
    private String confirmPassword;
}
