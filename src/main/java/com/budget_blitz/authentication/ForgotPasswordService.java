package com.budget_blitz.authentication;

import com.budget_blitz.authentication.request.ForgotPasswordRequest;
import com.budget_blitz.authentication.request.ResetCodeRequest;
import com.budget_blitz.authentication.request.ResetPasswordRequest;
import com.budget_blitz.authentication.response.AuthResponse;
import jakarta.mail.MessagingException;

public interface ForgotPasswordService {

    void forgotPassword(final ForgotPasswordRequest request) throws MessagingException;
    void verifyResetCode(final ResetCodeRequest request) throws MessagingException;
    AuthResponse resetPassword(final ResetPasswordRequest request);
}
