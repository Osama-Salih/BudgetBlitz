package com.budget_blitz.authentication;

import com.budget_blitz.authentication.request.*;
import com.budget_blitz.authentication.response.AuthResponse;
import jakarta.mail.MessagingException;

public interface AuthService {

    void register(final RegisterRequest request) throws MessagingException;
    AuthResponse login(final LoginRequest request);
    AuthResponse refresh(final RefreshRequest request);
    void activateAccount(final ActivateAccountRequest code) throws MessagingException;
}
