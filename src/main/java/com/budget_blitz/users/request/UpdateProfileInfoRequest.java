package com.budget_blitz.users.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
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

    @Size(
            min = 2,
            max = 50,
            message = "First name must be between 2 to 50 character length"
    )
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only letters")
    @Schema(
            description = "User's first name (optional). must be between 2 and 50 characters.",
            example = "Osama"
    )
    private String firstName;

    @Size(
            min = 2,
            max = 50,
            message = "Last name must be between 2 to 50 character length"
    )
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must contain only letters")
    @Schema(
            description = "User's last name (optional). must be between 2 and 50 characters.",
            example = "Salih"
    )
    private String lastName;
}

