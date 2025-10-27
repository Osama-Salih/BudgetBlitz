package com.budget_blitz.authentication;

import com.budget_blitz.authentication.request.ActivateAccountRequest;
import com.budget_blitz.authentication.request.LoginRequest;
import com.budget_blitz.authentication.request.RefreshRequest;
import com.budget_blitz.authentication.request.RegisterRequest;
import com.budget_blitz.authentication.response.AuthResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

public interface AuthService {

    void register(final RegisterRequest request) throws MessagingException;
    AuthResponse login(final LoginRequest request);
    AuthResponse refresh(final RefreshRequest request);
    void activateAccount(final ActivateAccountRequest code) throws MessagingException;
}
