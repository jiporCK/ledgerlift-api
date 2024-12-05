package com.example.ledgerlift.features.sociallogins;

import com.example.ledgerlift.features.sociallogins.sessionpayload.FacebookPayload;
import com.example.ledgerlift.features.sociallogins.sessionpayload.GooglePayload;
import jakarta.servlet.http.HttpServletRequest;

public interface SocialLoginService {

    void registerFacebookUser(FacebookPayload facebookPayload, final HttpServletRequest servletRequest);

    void registerGoogleUser(GooglePayload googlePayload, final HttpServletRequest servletRequest);

}
