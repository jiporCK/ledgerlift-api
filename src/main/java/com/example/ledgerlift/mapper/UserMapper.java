package com.example.ledgerlift.mapper;

import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.user.userdto.UserCreateRequest;
import com.example.ledgerlift.features.user.userdto.UserResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromUserCreateRequest(UserCreateRequest userCreateRequest);

    List<UserResponse> toUserResponses(List<User> users);

}
