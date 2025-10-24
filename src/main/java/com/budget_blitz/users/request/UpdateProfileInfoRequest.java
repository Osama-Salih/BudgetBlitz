package com.budget_blitz.users.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(
        name = "UpdateProfileInfoRequest",
        description = "Request payload for updating user's profile information"
)
public class UpdateProfileInfoRequest {
    @Schema(
            description = "User's first name (optional). Must be between 1 and 10 characters if provided.",
            example = "Osama"
    )
    @Size(min = 1, max = 10, message = "First name length must be between 1 to 10 characters")
    private String firstName;

    @Schema(
            description = "User's last name (optional). Must be between 1 and 10 characters if provided.",
            example = "Salih"
    )
    @Size(min = 1, max = 10, message = "Last name length must be between 1 to 10 characters")
    private String lastName;
}

