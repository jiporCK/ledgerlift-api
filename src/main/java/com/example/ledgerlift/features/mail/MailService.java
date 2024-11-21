package com.example.ledgerlift.features.mail;

import com.example.ledgerlift.base.BasedMessage;
import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.mail.dto.MailRequest;

public interface MailService {

    void sendEmail(MailRequest mailRequest);

    BasedMessage sendTokenForEmailVerification(String url, User user, String verificationToken);

    BasedMessage sendTokenForPasswordRequest(String url, User user);

}
