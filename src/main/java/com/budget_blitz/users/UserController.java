package com.budget_blitz.users;

import com.budget_blitz.users.request.ChangePasswordRequest;
import com.budget_blitz.users.request.UpdateProfileInfoRequest;
import com.budget_blitz.users.response.ProfileInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for managing user account and profile information")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Display the user's profile information",
            description = "Allows an authenticated user to get their profile information."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile info displayed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProfileInfoResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - user must be logged in",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/me")
    public ResponseEntity<ProfileInfoResponse> getProfileInfo(final Authentication principal) {
        return ResponseEntity.ok(this.userService.getProfileInfo(getUserId(principal)));
    }

    @Operation(
            summary = "Update user's profile information",
            description = "Allows an authenticated user to update their first name and last name."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProfileInfoResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error or invalid input",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - user must be logged in",
                    content = @Content(mediaType = "application/json")
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "JSON object containing the fields to update",
            content = @Content(
                    schema = @Schema(implementation = UpdateProfileInfoRequest.class)
            )
    )
    @PatchMapping("/me")
    public ResponseEntity<ProfileInfoResponse> updateProfileInfo(
            @RequestBody
            @Valid
            final UpdateProfileInfoRequest request,
            final Authentication principal) {
        return ResponseEntity.ok(this.userService.updateProfileInfo(request, getUserId(principal)));
    }

    @Operation(
            summary = "Update user's password",
            description = "Allows an authenticated user to update their password."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Password updated successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error or invalid input",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - user must be logged in",
                    content = @Content(mediaType = "application/json")
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "204 status code specifying that no content returned",
            content = @Content(
                    schema = @Schema(implementation = ChangePasswordRequest.class)
            )
    )
    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @RequestBody
            @Valid
            final ChangePasswordRequest request, final Authentication principal) {
        this.userService.changePassword(request, getUserId(principal));
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update user's account state",
            description = "Allows an authenticated user to deactivate their account."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Account deactivated successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - user must be logged in",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PatchMapping("/deactivate-me")
    public ResponseEntity<Void> deactivateAccount(final Authentication principal) {
        this.userService.deactivateAccount(getUserId(principal));
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update user's account state",
            description = "Allows an authenticated user to reactivate their account."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Account reactivate successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - user must be logged in",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PatchMapping("/reactivate-me")
    public ResponseEntity<Void> reactivateAccount(final Authentication principal) {
        this.userService.reactivateAccount(getUserId(principal));
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete user's account",
            description = "Allows an authenticated user to delete their account."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Account delete successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - user must be logged in",
                    content = @Content(mediaType = "application/json")
            )
    })
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAccount(final Authentication principal) {
        this.userService.deleteAccount(getUserId(principal));
        return ResponseEntity.noContent().build();
    }

    private Integer getUserId(final Authentication principal) {
        return ((User) principal.getPrincipal()).getId();
    }
}
