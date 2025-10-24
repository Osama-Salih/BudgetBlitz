package com.budget_blitz.users;

import com.budget_blitz.users.request.UpdateProfileInfoRequest;
import com.budget_blitz.users.response.ProfileInfoResponse;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

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
}
