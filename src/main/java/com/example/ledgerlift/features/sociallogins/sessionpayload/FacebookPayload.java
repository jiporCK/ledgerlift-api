package com.example.ledgerlift.features.sociallogins.sessionpayload;

import com.example.ledgerlift.features.sociallogins.sessionpayload.facebook.UserFacebook;
import com.example.ledgerlift.features.sociallogins.sessionpayload.facebook.UserToken;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FacebookPayload {

    private UserFacebook user;

    private UserToken token;

    private LocalDateTime expires;

}

