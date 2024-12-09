package com.example.ledgerlift.features.sociallogins;

import com.example.ledgerlift.domain.SocialLogin;
import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.mail.MailService;
import com.example.ledgerlift.features.sociallogins.sessionpayload.FacebookPayload;
import com.example.ledgerlift.features.sociallogins.sessionpayload.GooglePayload;
import com.example.ledgerlift.features.user.UserController;
import com.example.ledgerlift.features.user.UserRepository;
import com.example.ledgerlift.features.user.dto.RegistrationRequest;
import com.example.ledgerlift.utils.MailUtils;
import com.example.ledgerlift.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialLoginServiceImpl implements SocialLoginService{

    private final SocialLoginRepository socialLoginRepository;
    private final UserController userController;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final MailService mailService;

    @Transactional
    @Override
    public void registerFacebookUser(FacebookPayload facebookPayload, final HttpServletRequest servletRequest) {
        String id = facebookPayload.getUser().getId();
        String name = facebookPayload.getUser().getName();
        String[] parts = name.split(" ");
        String firstName = parts[0];
        String lastName = parts[1];
        String email = facebookPayload.getUser().getEmail();
        String password = Utils.generateUuid();

        String picUrl = facebookPayload.getUser().getPicture().getData().getUrl();
        boolean isSilhouette = facebookPayload.getUser().getPicture().getData().isSilhouette();

        RegistrationRequest request = RegistrationRequest.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .username(name)
                .build();

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setAvatar(picUrl);
            userRepository.save(user);
        } else {
            // Use the `isSocialLogin` flag to skip email verification
            userController.createUser(request, servletRequest, true);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User has not been found"
                ));

        user.setAvatar(picUrl);
        userRepository.save(user);

        SocialLogin socialLogin = new SocialLogin();
        socialLogin.setUser(user);
        socialLogin.setProvider("facebook");

        String payload;
        try {
            payload = objectMapper.writeValueAsString(facebookPayload);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error converting FacebookPayload to JSON string",
                    e
            );
        }

        socialLogin.setPayload(payload);

        socialLoginRepository.save(socialLogin);

        mailService.welcomeMessage(user);

    }


    @Transactional
    @Override
    public void registerGoogleUser(GooglePayload googlePayload, HttpServletRequest servletRequest) {
        String id = googlePayload.getUser().getId();
        String name = googlePayload.getUser().getName();
        String[] parts = name.split(" ");
        String firstName = parts[0];
        String lastName = parts[1];
        String email = googlePayload.getUser().getEmail();
        String password = name + MailUtils.generateDigitsToken();
        String image = googlePayload.getUser().getImage();

        RegistrationRequest request = RegistrationRequest.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .username(name)
                .build();

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setAvatar(image);
            userRepository.save(user);
        } else {
            // Use the `isSocialLogin` flag to skip email verification
            userController.createUser(request, servletRequest, true);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User has not been found"
                ));

        user.setAvatar(image);
        userRepository.save(user);

        SocialLogin socialLogin = new SocialLogin();
        socialLogin.setUser(user);
        socialLogin.setProvider("google");

        String payload;
        try {
            payload = objectMapper.writeValueAsString(googlePayload);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error converting GooglePayload to JSON string",
                    e
            );
        }

        socialLogin.setPayload(payload);

        socialLoginRepository.save(socialLogin);

        mailService.welcomeMessage(user);

    }

}
