package com.example.ledgerlift.features.sociallogins;

import com.example.ledgerlift.domain.SocialLogin;
import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.sociallogins.sessionpayload.FacebookPayload;
import com.example.ledgerlift.features.sociallogins.sessionpayload.GooglePayload;
import com.example.ledgerlift.features.sociallogins.sessionpayload.facebook.Picture;
import com.example.ledgerlift.features.sociallogins.sessionpayload.facebook.PictureData;
import com.example.ledgerlift.features.sociallogins.sessionpayload.facebook.UserFacebook;
import com.example.ledgerlift.features.sociallogins.sessionpayload.facebook.UserToken;
import com.example.ledgerlift.features.sociallogins.sessionpayload.google.UserGoogle;
import com.example.ledgerlift.features.user.UserController;
import com.example.ledgerlift.features.user.UserRepository;
import com.example.ledgerlift.features.user.dto.RegistrationRequest;
import com.example.ledgerlift.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialLoginServiceImpl implements SocialLoginService{

    private final SocialLoginRepository socialLoginRepository;
    private final UserController userController;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;


    @Override
    public void registerFacebookUser(FacebookPayload facebookPayload, final HttpServletRequest servletRequest) {

        String id = facebookPayload.getUser().getId();
        String name = facebookPayload.getUser().getName();
        String firstName = name.substring(0, name.indexOf(" "));
        String lastName = name.substring(name.indexOf(" ") + 1);
        String email = facebookPayload.getUser().getEmail();
        String password = Utils.generateUuid();

        String picUrl = facebookPayload.getUser().getPicture().getData().getUrl();
        boolean isSilhouette = facebookPayload.getUser().getPicture().getData().isSilhouette();

        String accessToken = facebookPayload.getToken().getAccessToken();
        int expiresIn = facebookPayload.getToken().getExpiresIn();
        String token_type = facebookPayload.getToken().getTokenType();
        LocalDateTime expires = facebookPayload.getExpires();

        RegistrationRequest request = RegistrationRequest.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .phoneNumber(null)
                .username(name)
                .build();

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // Update user fields like avatar, etc.
            user.setAvatar(picUrl);
            userRepository.save(user);
        } else {
            // Create new user if it doesn't exist
            userController.createUser(request, servletRequest);
        }


        if (userRepository.existsByEmail(email)) {

            User user = userRepository.findByEmail(email)
                    .orElseThrow(
                            () -> new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    "User has not been found"
                            )
                    );
            user.setAvatar(picUrl);

            SocialLogin socialLogin = new SocialLogin();
            socialLogin.setUser(user);
            socialLogin.setProvider("facebook");

            FacebookPayload facebookPayload1 = FacebookPayload.builder()
                    .user(
                            UserFacebook.builder()
                                    .id(id)
                                    .name(name)
                                    .email(email)
                                    .picture(
                                            Picture.builder()
                                                    .data(
                                                            PictureData.builder()
                                                            .url(picUrl)
                                                            .isSilhouette(isSilhouette)
                                                            .build())
                                                    .build()
                                    )
                                    .build()
                    )
                    .token(
                            UserToken.builder()
                                    .accessToken(accessToken)
                                    .expiresIn(expiresIn)
                                    .tokenType(token_type)
                                    .build()
                    )
                    .expires(expires).build();

            // Convert FacebookPayload object to JSON string
            String payload;
            try {
                payload = objectMapper.writeValueAsString(facebookPayload1);
            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error converting FacebookPayload to JSON string",
                        e
                );
            }

            socialLogin.setPayload(payload);

            socialLoginRepository.save(socialLogin);

        }

    }

    @Override
    public void registerGoogleUser(GooglePayload googlePayload, HttpServletRequest servletRequest) {

        String id = googlePayload.getUser().getId();
        String name = googlePayload.getUser().getName();
        String firstName = name.substring(0, name.indexOf(" "));
        String lastName = name.substring(name.indexOf(" ") + 1);
        String email = googlePayload.getUser().getEmail();
        String password = Utils.generateUuid();
        String image = googlePayload.getUser().getImage();

        LocalDateTime expires = googlePayload.getExpires();

        RegistrationRequest request = RegistrationRequest.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .phoneNumber(null)
                .username(name)
                .build();

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // Update user fields like avatar, etc.
            user.setAvatar(image);
            userRepository.save(user);
        } else {
            // Create new user if it doesn't exist
            userController.createUser(request, servletRequest);
        }


        if (userRepository.existsByEmail(email)) {

            User user = userRepository.findByEmail(email)
                    .orElseThrow(
                            () -> new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    "User has not been found"
                            )
                    );

            user.setAvatar(image);
            userRepository.save(user);

            SocialLogin socialLogin = new SocialLogin();
            socialLogin.setUser(user);
            socialLogin.setProvider("google");

            GooglePayload googlePayload1 = GooglePayload.builder()
                    .user(UserGoogle.builder()
                            .id(id)
                            .name(name)
                            .email(email)
                            .image(image)
                            .build())
                    .expires(expires)
                    .build();

            // Convert FacebookPayload object to JSON string
            String payload;
            try {
                payload = objectMapper.writeValueAsString(googlePayload1);
            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error converting FacebookPayload to JSON string",
                        e
                );
            }

            socialLogin.setPayload(payload);

            socialLoginRepository.save(socialLogin);

        }

    }

}
