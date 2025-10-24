package com.budget_blitz.users.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(
        name = "ProfileInfoResponse",
        description = "Response model containing the user's profile information"
)
public class ProfileInfoResponse {

    @Schema(description = "User's id", example = "1")
    private Integer id;

    @Schema(description = "User's first name", example = "Osama")
    private String firstName;

    @Schema(description = "User's last name", example = "Salih")
    private String lastName;

    @Schema(description = "User's email address", example = "osama@budget.blitz.com")
    private String email;

    @Schema(description = "User's date of birth in YYYY-MM-DD format", example = "2000-06-06")
    private LocalDate dateOfBirth;
}
