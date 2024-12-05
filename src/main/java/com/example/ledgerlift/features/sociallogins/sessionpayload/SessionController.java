package com.example.ledgerlift.features.sociallogins.sessionpayload;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/facebook-payload")
    public void faceBookPayload(@RequestBody FacebookPayload facebookPayload, final HttpServletRequest request) {

        sessionService.facebookPayload(facebookPayload, request);

    }

    @PostMapping("/google-payload")
    public void googlePayload(@RequestBody GooglePayload googlePayload, final HttpServletRequest request) {

        sessionService.googlePayload(googlePayload, request);

    }

}
