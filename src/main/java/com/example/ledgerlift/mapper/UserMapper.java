package com.example.ledgerlift.mapper;

import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.user.dto.RegistrationRequest;
import com.example.ledgerlift.features.user.dto.UserDetail;
import com.example.ledgerlift.features.user.dto.UserResponse;
import com.example.ledgerlift.features.user.dto.UserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    User fromUserCreateRequest(RegistrationRequest registrationRequest);

    List<UserResponse> toUserResponseList(List<User> users);

    UserResponse toUserResponse(User user);

    User fromUserResponse(UserResponse response);

    @Mapping(target = "role", source = "roles")
    UserDetail toUserDetail(User user);

}
