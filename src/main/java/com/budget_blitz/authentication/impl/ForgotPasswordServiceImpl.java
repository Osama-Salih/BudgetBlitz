package com.budget_blitz.authentication.impl;

import com.budget_blitz.authentication.ForgotPasswordService;
import com.budget_blitz.authentication.request.ForgotPasswordRequest;
import com.budget_blitz.authentication.request.ResetCodeRequest;
import com.budget_blitz.authentication.request.ResetPasswordRequest;
import com.budget_blitz.authentication.response.AuthResponse;
import com.budget_blitz.email.EmailService;
import com.budget_blitz.email.EmailTemplateName;
import com.budget_blitz.exception.BusinessException;
import com.budget_blitz.exception.ErrorCode;
import com.budget_blitz.security.JwtService;
import com.budget_blitz.users.Token;
import com.budget_blitz.users.TokenRepository;
import com.budget_blitz.users.User;
import com.budget_blitz.users.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final AuthServiceImpl authServiceImpl;

    private final EmailService emailService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void forgotPassword(final ForgotPasswordRequest request) throws MessagingException {
        final User user = this.userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_EMAIL_NOT_FOUND));

        sendResetPasswordEmail(user);
    }

    @Override
    @Transactional
    public void verifyResetCode(final ResetCodeRequest request) throws MessagingException {

        final Token savedToken = this.tokenRepository.findAll()
                .stream()
                .filter(token -> this.passwordEncoder.matches(request.getResetCode(), token.getToken()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Invalid reset code"));

        if (savedToken.getValidatedAt() != null) {
            throw new BusinessException(ErrorCode.RESET_CODE_ALREADY_VERIFIED);
        }

        final User user = this.userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, savedToken.getUser().getId()));

        if (savedToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            sendResetPasswordEmail(user);
            throw new BusinessException(ErrorCode.EXPIRED_RESET_CODE);
        }
        savedToken.setValidatedAt(LocalDateTime.now());
        this.tokenRepository.save(savedToken);
    }

    @Override
    public AuthResponse resetPassword(final ResetPasswordRequest request) {
        final User user = this.userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_EMAIL_NOT_FOUND));

        final String encodedPassword = this.passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        this.userRepository.save(user);
        log.info("User reset his password successfully with email: {}", user.getEmail());

        final String accessToken = this.jwtService.generateAccessToken(user.getEmail());
        final String refreshToken = this.jwtService.generateRefreshToken(user.getEmail());
        final String tokenType = "Bearer ";

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(tokenType)
                .build();
    }


    private void sendResetPasswordEmail(final User user) throws MessagingException {
        final String resetCode = this.authServiceImpl.generateAndSaveToken(user);

        log.info("Reset code sent successfully with email: {}", user.getEmail());
        this.emailService.sendEmail(user.getEmail(), user.fullName(), EmailTemplateName.PASSWORD_RESET,
                resetCode,user.fullName() + ", here's your PIN " + resetCode + " (valid 15 minutes)");
    }
}