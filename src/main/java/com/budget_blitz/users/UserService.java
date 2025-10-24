package com.budget_blitz.users;

import com.budget_blitz.users.request.ChangePasswordRequest;
import com.budget_blitz.users.request.UpdateProfileInfoRequest;
import com.budget_blitz.users.response.ProfileInfoResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    ProfileInfoResponse getProfileInfo(final Integer userId);
    ProfileInfoResponse updateProfileInfo(final UpdateProfileInfoRequest request, final Integer userId);
    void changePassword(final ChangePasswordRequest request, final Integer userId);
    void deactivateAccount(final Integer userId);
    void reactivateAccount(final Integer userId);
    void deleteAccount(final Integer userId);
}
