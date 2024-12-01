package com.example.ledgerlift.features.user;

import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.mail.verificationToken.VerificationToken;
import com.example.ledgerlift.features.media.dto.ImageRequest;
import com.example.ledgerlift.features.user.dto.RegistrationRequest;
import com.example.ledgerlift.features.user.dto.UserDetail;
import com.example.ledgerlift.features.user.dto.UserResponse;
import com.example.ledgerlift.features.user.dto.UserUpdateRequest;

import java.util.List;

public interface UserService {

    UserResponse createUser(RegistrationRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserByUuid(String uuid);

    void uploadProfile(String uuid, ImageRequest profile);

    String validateVerificationToken(String token);

    void saveUserVerificationToken(User theUser, String verificationToken, VerificationToken.TokenType tokenType);

    void updateUser(String uuid, UserUpdateRequest request);

    void blockUserByUuid(String uuid);

    void unblockUserByUuid(String uuid);

    void deleteUserByUuid(String uuid);

    UserDetail getUserInfo(String uuid);
}
