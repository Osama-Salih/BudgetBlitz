package com.budget_blitz.authentication.impl;

import com.budget_blitz.authentication.AuthService;
import com.budget_blitz.authentication.request.*;
import com.budget_blitz.authentication.response.AuthResponse;
import com.budget_blitz.email.EmailService;
import com.budget_blitz.email.EmailTemplateName;
import com.budget_blitz.exception.BusinessException;
import com.budget_blitz.exception.ErrorCode;
import com.budget_blitz.role.Role;
import com.budget_blitz.role.RoleRepository;
import com.budget_blitz.security.JwtService;
import com.budget_blitz.users.*;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    public AuthResponse login(final LoginRequest request) {
        final Authentication auth = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        final User user = (User) auth.getPrincipal();
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

    @Override
    @Transactional
    public void register(final RegisterRequest request) throws MessagingException {
       checkEmail(request.getEmail());

       final Role role = this.roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("Role user not found"));

       final User user = this.userMapper.toUser(request, passwordEncoder);
       user.setRoles(Set.of(role));
       this.userRepository.save(user);

       log.info("User registered successfully with email: {}", user.getEmail());
       sendValidationEmail(user);
    }

    @Override
    public void activateAccount(final ActivateAccountRequest request) throws MessagingException {
        final Token savedToken = this.tokenRepository.findAll()
                .stream()
                .filter(token -> this.passwordEncoder.matches(request.getCode(), token.getToken()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Invalid token"));

        if (savedToken.getValidatedAt() != null) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_VERIFIED);
        }

        final User user = this.userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, savedToken.getUser().getId()));
        if (savedToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            sendValidationEmail(user);
            throw new BusinessException(ErrorCode.EXPIRED_ACTIVATION_CODE);
        }
        savedToken.setValidatedAt(LocalDateTime.now());
        this.tokenRepository.save(savedToken);

        user.setEmailVerified(true);
        user.setEnabled(true);
        this.userRepository.save(user);
    }

    private void sendValidationEmail(final User user) throws MessagingException {
        final String newToken = generateAndSaveToken(user);
        this.emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                newToken,
                "Activate Your BudgetBlitz Account – Use Your Verification Code");
    }

    public String generateAndSaveToken(final User user) {
         final String generatedCode = generateActivationCode(6);
         final Token token = Token.builder()
                .token(this.passwordEncoder.encode(generatedCode))
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        this.tokenRepository.save(token);
        return generatedCode;
    }

    private String generateActivationCode(final int length) {
        final String sourceCode = "0123456789";
        final StringBuilder stringBuilder = new StringBuilder();
        final SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int nextIndex = secureRandom.nextInt(sourceCode.length());
            stringBuilder.append(sourceCode.charAt(nextIndex));
        }
        return stringBuilder.toString();
    }

    private void checkEmail(final String email) {
        boolean emailExists = this.userRepository.existsByEmailIgnoreCase(email);
        if (emailExists) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }
}
