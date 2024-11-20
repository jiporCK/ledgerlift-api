package com.example.ledgerlift.features.user;

import com.example.ledgerlift.base.BasedMessage;
import com.example.ledgerlift.features.user.userdto.UserCreateRequest;
import com.example.ledgerlift.features.user.userdto.UserResponse;

import java.util.List;

public interface UserService {

    String createUser(UserCreateRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserByUuid(String uuid);

    BasedMessage blockUser(String uuid);

    BasedMessage unblockUser(String uuid);

    BasedMessage uploadProfile(String uuid, String profile);

}
