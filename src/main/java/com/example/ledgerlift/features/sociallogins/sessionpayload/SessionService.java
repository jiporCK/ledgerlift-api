package com.example.ledgerlift.features.sociallogins.sessionpayload;

import com.example.ledgerlift.features.sociallogins.SocialLoginRepository;
import com.example.ledgerlift.features.sociallogins.SocialLoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SocialLoginService socialLoginService;

    public void facebookPayload(FacebookPayload facebookPayload, final HttpServletRequest request) {

        socialLoginService.registerFacebookUser(facebookPayload, request);

    }

    public void googlePayload(GooglePayload googlePayload, final HttpServletRequest request) {

        socialLoginService.registerGoogleUser(googlePayload, request);

    }
}
