package com.budget_blitz.authentication.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(
        name = "RefreshRequest",
        description = "Request object with only one filed for refresh token"
)
public class RefreshRequest {

    @NotBlank(message = "Refresh token required")
    @Schema(
            description = "The refresh token used to obtain a new access token",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private String refreshToken;
}
