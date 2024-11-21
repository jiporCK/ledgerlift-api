package com.example.ledgerlift.event.listener;

import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.event.RegistrationCompleteEvent;
import com.example.ledgerlift.features.mail.MailService;
import com.example.ledgerlift.features.mail.verificationToken.VerificationToken;
import com.example.ledgerlift.features.user.UserService;
import com.example.ledgerlift.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final MailService mailService;
    private final UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // 1. Get the newly registered user
        User theUser = event.getUser();

        // 2. Create a verification token for the user
        String verificationToken = Utils.generateDigitsToken();

        // 3. Save the verification token for the user
        userService.saveUserVerificationToken(theUser, verificationToken, VerificationToken.TokenType.EMAIL_VERIFICATION);

        // 4. Build the verification URL to be sent to the user
        String url = event.getApplicationUrl() + "/api/v1/register/verify-email?token=" + verificationToken;

        // 5. Send the email
        mailService.sendTokenForEmailVerification(url, theUser, verificationToken);

        log.info("Click the link to verify your registration: {}", url);
    }
}
