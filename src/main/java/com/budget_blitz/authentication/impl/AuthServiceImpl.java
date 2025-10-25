package com.budget_blitz.authentication.impl;

import com.budget_blitz.authentication.AuthService;
import com.budget_blitz.authentication.request.LoginRequest;
import com.budget_blitz.authentication.request.RefreshRequest;
import com.budget_blitz.authentication.request.RegisterRequest;
import com.budget_blitz.authentication.response.AuthResponse;
import com.budget_blitz.exception.BusinessException;
import com.budget_blitz.exception.ErrorCode;
import com.budget_blitz.secuirty.JwtService;
import com.budget_blitz.users.User;
import com.budget_blitz.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void register(final RegisterRequest request) {
        checkEmail(request.getEmail());
        // complete after adding the role
    }

    @Override
    public AuthResponse login(final LoginRequest request) {
        final Authentication auth = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        final User user = this.userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        final boolean passwordMatch = this.passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!passwordMatch) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }
        final String accessToken = this.jwtService.generateAccessToken(user.getEmail());
        final String refreshToken = this.jwtService.generateRefreshToken(user.getEmail());
        final String tokenType = "Bearer ";

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(tokenType)
                .build();
    }

    @Override
    public AuthResponse refresh(final RefreshRequest request) {
       final String newAccessToken = this.jwtService.refreshAccessToken(request.getRefreshToken());
       final String tokenType = "Bearer ";

       return AuthResponse.builder()
               .accessToken(newAccessToken)
               .refreshToken(request.getRefreshToken())
               .tokenType(tokenType)
               .build();
    }

    private void checkEmail(final String email) {
        boolean emailExists = this.userRepository.existsByEmailIgnoreCase(email);
        if (emailExists) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }
}
