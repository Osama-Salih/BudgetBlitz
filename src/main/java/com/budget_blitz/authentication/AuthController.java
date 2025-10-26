package com.budget_blitz.authentication;

import com.budget_blitz.authentication.request.LoginRequest;
import com.budget_blitz.authentication.request.RefreshRequest;
import com.budget_blitz.authentication.request.RegisterRequest;
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
}
