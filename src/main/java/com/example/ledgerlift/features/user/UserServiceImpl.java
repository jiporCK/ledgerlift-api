package com.example.ledgerlift.features.user;

import com.example.ledgerlift.base.BasedMessage;
import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.mail.verificationToken.VerificationToken;
import com.example.ledgerlift.features.mail.verificationToken.VerificationTokenRepository;
import com.example.ledgerlift.features.user.dto.RegistrationRequest;
import com.example.ledgerlift.features.user.dto.UserResponse;
import com.example.ledgerlift.mapper.UserMapper;
import com.example.ledgerlift.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final VerificationTokenRepository verificationTokenRepository;

    @Transactional
    @Override
    public UserResponse createUser(RegistrationRequest request) {

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
        user.setIsAccountVerified(false);
        user.setIsAnonymous(false);

        return userMapper.toUserResponse(user);
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

//    @Transactional
//    @Override
//    public BasedMessage enableByUuid(String uuid) {
//        if (userRepository.existsByUuid(uuid)) {
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND,
//                    "User has not been found"
//            );
//        }
//        userRepository.enableByUuid(uuid);
//
//        return new BasedMessage("User has been enabled");
//    }
//
//    @Transactional
//    @Override
//    public BasedMessage disableByUuid(String uuid) {
//        if (userRepository.existsByUuid(uuid)) {
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND,
//                    "User has not been found"
//            );
//        }
//        userRepository.disableByUuid(uuid);
//
//        return new BasedMessage("User has been disabled");
//    }

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

    @Override
    public String validateVerificationToken(String token) {

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken.isExpired()) {
            return "Token is already expired";
        }

        if (verificationToken.getIsUsed()) {
            return "Token is already used";
        }

        verificationToken.setIsUsed(true);
        verificationTokenRepository.save(verificationToken);

        return "valid";
    }
    @Transactional
    @Override
    public void saveUserVerificationToken(User theUser, String token, VerificationToken.TokenType type) {

        VerificationToken verificationToken = new VerificationToken(token, VerificationToken.TokenType.EMAIL_VERIFICATION);

        verificationToken.setUser(theUser);

        verificationTokenRepository.save(verificationToken);

    }

}
