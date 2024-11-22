package com.example.ledgerlift.features.mail;

import com.example.ledgerlift.domain.Organization;
import com.example.ledgerlift.domain.Receipt;
import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.mail.dto.MailRequest;
import com.example.ledgerlift.features.receipt.dto.ReceiptResponse;

public interface MailService {

    void sendEmail(MailRequest mailRequest);

    void sendTokenForEmailVerification(User user, String verificationToken);

    void sendTokenForPasswordRequest(String url, User user);

    void welcomeMessage(User user);

    void welcomeOrganization(Organization organization);

    void sendDonationReceipt(User user, Receipt receipt);

    void thankYouEmail(User user, String type);

    void accountNotification(User user, String relatedUpdate);

}
