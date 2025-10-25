package com.budget_blitz.authentication.impl;

import com.budget_blitz.authentication.AuthService;
import com.budget_blitz.authentication.request.LoginRequest;
import com.budget_blitz.authentication.request.RefreshRequest;
import com.budget_blitz.authentication.request.RegisterRequest;
import com.budget_blitz.authentication.response.AuthResponse;
import com.budget_blitz.exception.BusinessException;
import com.budget_blitz.exception.ErrorCode;
import com.budget_blitz.role.Role;
import com.budget_blitz.role.RoleRepository;
import com.budget_blitz.secuirty.JwtService;
import com.budget_blitz.users.User;
import com.budget_blitz.users.UserMapper;
import com.budget_blitz.users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void register(final RegisterRequest request) {
       checkEmail(request.getEmail());

       final Role role = this.roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("Role user not found"));

       final User user = this.userMapper.toUser(request);
       user.setRoles(Set.of(role));
       this.userRepository.save(user);

       log.info("User registered successfully with email: {}", user.getEmail());
//       sendValidationEmail(user);
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
