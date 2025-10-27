package com.budget_blitz.authentication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(
        name = "ActivateAccountRequest",
        description = "Request object containing the activation code to verify and activate the user account"
)
public class ActivateAccountRequest {

    @NotBlank(message = "Code required")
    @Length(min = 6, max = 6, message = "The code must be 6 digits long")
    @Schema(
            description = "Activation code sent to the user's email. Must be exactly 6 digits long.",
            example = "123456",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String code;
}
