package com.example.ledgerlift.features.user;

import com.example.ledgerlift.base.BasedMessage;
import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.user.userdto.UserCreateRequest;
import com.example.ledgerlift.features.user.userdto.UserResponse;
import com.example.ledgerlift.mapper.UserMapper;
import com.example.ledgerlift.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public String createUser(UserCreateRequest request) {

        User user = userMapper.fromUserCreateRequest(request);

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email: " + user.getEmail() +" is already in use"
            );
        }
        user.setUuid(Utils.generateUuid());
        user.setIsProfiledVisibility(false);
        user.setIsEmailVerified(false);

        userRepository.save(user);

        return "User is created successfully";
    }

    @Override
    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No users found");
        }

        return userMapper.toUserResponseList(users);
    }

    @Override
    public UserResponse getUserByUuid(String uuid) {
        return null;
    }

    @Override
    public BasedMessage blockUser(String uuid) {
        return null;
    }

    @Override
    public BasedMessage unblockUser(String uuid) {
        return null;
    }

    @Override
    public BasedMessage uploadProfile(String uuid, String profile) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setAvatar(profile);
        userRepository.save(user);

        return BasedMessage.builder()
                .message("Profile uploaded successfully")
                .build();

    }
}
