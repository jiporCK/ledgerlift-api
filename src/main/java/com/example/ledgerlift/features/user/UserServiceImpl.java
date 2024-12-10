package com.example.ledgerlift.features.user;

import com.example.ledgerlift.base.BasedMessage;
import com.example.ledgerlift.domain.Media;
import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.mail.MailService;
import com.example.ledgerlift.features.mail.verificationToken.VerificationToken;
import com.example.ledgerlift.features.mail.verificationToken.VerificationTokenRepository;
import com.example.ledgerlift.features.media.MediaRepository;
import com.example.ledgerlift.features.media.MediaService;
import com.example.ledgerlift.features.media.dto.ImageRequest;
import com.example.ledgerlift.features.media.dto.MediaResponse;
import com.example.ledgerlift.features.user.dto.RegistrationRequest;
import com.example.ledgerlift.features.user.dto.UserDetailResponse;
import com.example.ledgerlift.features.user.dto.UserResponse;
import com.example.ledgerlift.features.user.dto.UserUpdateRequest;
import com.example.ledgerlift.features.user.forgetpassworddto.PasswordResetRequest;
import com.example.ledgerlift.mapper.MediaMapper;
import com.example.ledgerlift.mapper.UserMapper;
import com.example.ledgerlift.utils.MailUtils;
import com.example.ledgerlift.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;
    private final MediaService mediaService;
    private final MailService mailService;

    @Override
    public UserResponse createUser(RegistrationRequest request) {

        User user = userMapper.fromUserCreateRequest(request);

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email: " + user.getEmail() +" is already in use"
            );
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username is already in use, Please use other name"
            );
        }

        user.setUuid(Utils.generateUuid());
        user.setIsProfiledVisibility(false);
        user.setIsEmailVerified(false);
        user.setIsAccountVerified(false);
        user.setIsAnonymous(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

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

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "User not found"
                        )
                );

        return userMapper.toUserResponse(user);
    }

    @Override
    public void uploadProfile(String uuid, ImageRequest profile) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        user.setAvatar(profile.image());

        MediaResponse mediaResponse = mediaService.getMediaByName(profile.image());
        Media media = mediaMapper.toMediaResponse(mediaResponse);
        media.setUser(user);
        mediaRepository.save(media);

        user.setMedia(media);
        userRepository.save(user);

        BasedMessage.builder()
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

    @Override
    public void updateUser(String uuid, UserUpdateRequest request) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found")
                );

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUsername(request.username());
        user.setPhoneNumber(request.phoneNumber());

        userRepository.save(user);

    }

    @Transactional
    @Override
    public void blockUserByUuid(String uuid) {

        User user = userRepository.findByUuid(uuid)
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found")
                        );

        if (user.getIsBlocked().equals(true)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has already been blocked");
        }

        userRepository.blockByUuid(uuid);

    }

    @Transactional
    @Override
    public void unblockUserByUuid(String uuid) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found")
                );

        if (user.getIsBlocked().equals(false)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has already been unblocked");
        }

        userRepository.unBlockByUuid(uuid);
    }

    @Override
    public void deleteUserByUuid(String uuid) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(
                        ()  -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found")
                );

        userRepository.delete(user);

    }

    @Override
    public UserDetailResponse getUserInfo(String uuid) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found")
                );

        return userMapper.toUserDetail(user);

    }

    @Override
    public void updateUserDOB(String uuid, LocalDateTime birthday) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "User has not been found"
                        )
                );

        user.setDateOfBirth(birthday);

        userRepository.save(user);

    }

    @Override
    public void verifyUser(String uuid, VerificationToken.TokenType tokenType) {

    }

    @Override
    public void saveForgetPasswordToken(User theUser, String forgetPasswordToken, VerificationToken.TokenType tokenType) {

        VerificationToken token = new VerificationToken(forgetPasswordToken, VerificationToken.TokenType.FORGET_PASSWORD);

        token.setUser(theUser);

        verificationTokenRepository.save(token);

    }

    @Override
    public String forgetPassword(String email, final HttpServletRequest servletRequest) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found")
                );

        String token = MailUtils.generateDigitsToken();

        saveForgetPasswordToken(user, token, VerificationToken.TokenType.FORGET_PASSWORD);

        passwordResetLink(user, Utils.getApplicationUrl(servletRequest), token);

        return token;

    }

    @Override
    public void passwordResetLink(User user, String appUrl, String forgetPasswordToken) {

        // replace this with reset password frontend
        String url = appUrl + "/api/v1/users/" + user.getEmail() + "/password-reset?token=" + forgetPasswordToken;

        mailService.sendTokenForPasswordRequest(url, user);

    }

    @Override
    public String validateForgetPasswordToken(String token) {
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

    @Override
    public void resetUserPassword(PasswordResetRequest request) {

        if (!userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password must be matched"
            );
        }

        User user = userRepository.findByEmail(request.email())
                        .orElseThrow(
                                ()  -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found")
                        );

        user.setPassword(passwordEncoder.encode(request.newPassword()));

        userRepository.save(user);

        String relatedUpdate = "Reset Password";

        mailService.accountNotification(user, relatedUpdate);

    }
}
