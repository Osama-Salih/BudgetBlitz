package com.budget_blitz.authentication.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(
        name = "AuthResponse",
        description = "Authentication response containing access and refresh tokens."
)
public class AuthResponse {

    @Schema(
            description = "JWT access token used to authorize requests. Usually expires after a short period (15 minutes).",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private String accessToken;

    @Schema(
            description = "Type of the token provided, usually 'Bearer'.",
            example = "Bearer "
    )
    private String tokenType;

    @Schema(
            description = "Refresh token used to obtain a new access token when the old one expires. Typically long-lived.",
            example = "dGhpcy1pcy1hLXNhbXBsZS1yZWZyZXNoLXRva2Vu"
    )
    private String refreshToken;
}
