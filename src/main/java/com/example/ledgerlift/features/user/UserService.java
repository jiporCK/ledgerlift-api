package com.example.ledgerlift.features.user;

import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.user.userdto.UserCreateRequest;
import com.example.ledgerlift.features.user.userdto.UserResponse;

import java.util.List;

public interface UserService {

    String createUser(UserCreateRequest request);

    List<UserResponse> getAllUsers();
}
