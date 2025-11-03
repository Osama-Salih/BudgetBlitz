package com.budget_blitz.users.impl;

import com.budget_blitz.exception.BusinessException;
import com.budget_blitz.exception.ErrorCode;
import com.budget_blitz.users.*;
import com.budget_blitz.users.request.ChangePasswordRequest;
import com.budget_blitz.users.request.UpdateProfileInfoRequest;
import com.budget_blitz.users.response.ProfileInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(final String userEmail) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public ProfileInfoResponse getProfileInfo(final Integer userId) {
        final User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));
        return this.userMapper.toProfileInfoResponse(user);
    }

    @Override
    public ProfileInfoResponse updateProfileInfo(final UpdateProfileInfoRequest request, final Integer userId) {
        final User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        this.userMapper.updateProfileInfo(request, user);
        this.userRepository.save(user);
        return userMapper.toProfileInfoResponse(user);
    }

    @Override
    public void changePassword(final ChangePasswordRequest request, final Integer userId) {
        final User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        final boolean isCurrentPasswordMatches = this.passwordEncoder.matches(request.getCurrentPassword(), user.getPassword());
        if (!isCurrentPasswordMatches) {
            throw new BusinessException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }

        final String encodedPassword = this.passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        this.userRepository.save(user);
    }

    @Override
    public void deactivateAccount(final Integer userId) {
        final User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        if (!user.isEnabled()) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_DEACTIVATE);
        }

        user.setEnabled(false);
        this.userRepository.save(user);
    }

    @Override
    public void reactivateAccount(final Integer userId) {
        final User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        if (user.isEnabled()) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_ACTIVATE);
        }

        user.setEnabled(true);
        this.userRepository.save(user);
    }

    @Override
    public void deleteAccount(final Integer userId) {
        this.deactivateAccount(userId);
    }
}
