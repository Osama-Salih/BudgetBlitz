package com.budget_blitz.authentication;

import com.budget_blitz.authentication.request.*;
import com.budget_blitz.authentication.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for managing user authentication")
public class AuthController {

    private final AuthService authService;
    private final ForgotPasswordService forgotPasswordService;

    @Operation(
            summary = "Register a new user",
            description = "Allows a new user to register by providing required credentials and information."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully registered",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation errors or invalid input",
                    content = @Content(mediaType = "application/json")
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "User registration details",
            content = @Content(
                    schema = @Schema(implementation = RegisterRequest.class)
            )
    )
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody
            @Valid
            final RegisterRequest request) throws MessagingException {
        this.authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "User login",
            description = "Allows an existing user to login with email and password and receive access and refresh tokens."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully logged in",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "unauthenticated - invalid credentials",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error or invalid input",
                    content = @Content(mediaType = "application/json")
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Login credentials",
            content = @Content(
                    schema = @Schema(implementation = LoginRequest.class)
            )
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody
            @Valid
            final LoginRequest request) {
        return ResponseEntity.ok(this.authService.login(request));
    }

    @Operation(
            summary = "Refresh access token",
            description = "Allows a user to refresh their access-token using a valid refresh-token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Access token successfully refreshed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - invalid or expired refresh token",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error or invalid input",
                    content = @Content(mediaType = "application/json")
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Refresh token request",
            content = @Content(
                    schema = @Schema(implementation = RefreshRequest.class)
            )
    )
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestBody
            @Valid
            final RefreshRequest request) {
        return ResponseEntity.ok(this.authService.refresh(request));
    }

    @Operation(
            summary = "Activate user account",
            description = "Activates a user's account using the 6-digit activation code sent to their email."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account successfully activated"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired activation code",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json"))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Activation code request body containing a valid 6-digit code",
            required = true,
            content = @Content(schema = @Schema(implementation = ActivateAccountRequest.class))
    )
    @PostMapping("/activate-account")
    public ResponseEntity<Void> activateAccount(
            @RequestBody
            @Valid
            final ActivateAccountRequest request) throws MessagingException {
        this.authService.activateAccount(request);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Request password reset",
            description = "Sends a 6-digit verification code to the user's registered email address to start the password reset process.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User's email address for password reset request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ForgotPasswordRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "202", description = "Reset code sent successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found with the provided email", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error while sending the email", content = @Content)
            }
    )
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
            @RequestBody
            @Valid
            final ForgotPasswordRequest request) throws MessagingException {
        this.forgotPasswordService.forgotPassword(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(
            summary = "Verify password reset code",
            description = "Verifies the 6-digit code sent to the user's email before allowing password reset.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "6-digit code sent to the user's email",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ResetCodeRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "202", description = "Reset code verified successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid or expired reset code", content = @Content)
            }
    )
    @PostMapping("/verify-reset-code")
    public ResponseEntity<Void> verifyResetCode(
            @RequestBody
            @Valid
            final ResetCodeRequest request) throws MessagingException {
        this.forgotPasswordService.verifyResetCode(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(
            summary = "Reset password",
            description = "Resets the user's password after successful verification of the reset code.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New password and confirmation password for the user",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ResetPasswordRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password reset successfully",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Passwords do not match or invalid reset request", content = @Content)
            }
    )
    @PatchMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(
            @RequestBody
            @Valid
            final ResetPasswordRequest request) {
        return ResponseEntity.ok(this.forgotPasswordService.resetPassword(request));
    }
}
