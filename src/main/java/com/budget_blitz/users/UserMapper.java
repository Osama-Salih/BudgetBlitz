package com.budget_blitz.users;

import com.budget_blitz.authentication.request.RegisterRequest;
import com.budget_blitz.users.request.UpdateProfileInfoRequest;
import com.budget_blitz.users.response.ProfileInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public ProfileInfoResponse toProfileInfoResponse(final User user) {
        return ProfileInfoResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }

    public void updateProfileInfo(final UpdateProfileInfoRequest request, final User user) {
       if (request.getFirstName() != null && !user.getFirstName().equals(request.getFirstName())) {
           user.setFirstName(request.getFirstName());
       }

        if (request.getLastName() != null && !user.getLastName().equals(request.getLastName())) {
            user.setLastName(request.getLastName());
        }
    }

    public User toUser(final RegisterRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .credentialsExpired(false)
                .enabled(false)
                .emailVerified(false)
                .expired(false)
                .build();
    }
}
