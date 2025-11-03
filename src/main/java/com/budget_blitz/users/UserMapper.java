package com.budget_blitz.users;

import com.budget_blitz.authentication.request.RegisterRequest;
import com.budget_blitz.users.request.UpdateProfileInfoRequest;
import com.budget_blitz.users.response.ProfileInfoResponse;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    public ProfileInfoResponse toProfileInfoResponse(final User user);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public void updateProfileInfo(final UpdateProfileInfoRequest request, @MappingTarget final User user);

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(request.getPassword()))")
    @Mapping(target = "accountLocked", constant = "false")
    @Mapping(target = "credentialsExpired", constant = "false")
    @Mapping(target = "enabled", constant = "false")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "expired", constant = "false")
    public User toUser(final RegisterRequest request, @Context PasswordEncoder passwordEncoder);
}