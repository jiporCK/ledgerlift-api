package com.example.ledgerlift.features.mail;

import com.example.ledgerlift.domain.Organization;
import com.example.ledgerlift.domain.Receipt;
import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.mail.dto.MailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${spring.mail.senderName}")
    private String senderName;

    @Value("${client-path}")
    private String clientUrl;

    @Override
    public void sendEmail(MailRequest request) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;

        try {
            messageHelper = new MimeMessageHelper(mimeMessage, true); // true enables multipart (HTML support)
            messageHelper.setFrom(mailFrom, senderName);
            messageHelper.setTo(request.to());
            messageHelper.setSubject(request.subject());
            messageHelper.setText(request.text(), true);

            mailSender.send(mimeMessage);
            log.info("Email sent successfully to {}", request.to());
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Error sending email to {}: {}", request.to(), e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void sendTokenForEmailVerification(User user, String verificationToken) {
        String subject = "Email Verification";
        String mailContent = "<div style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333;\">" +
                "<p style=\"font-size: 16px;\">Hi <strong>" + user.getUsername() + "</strong>,</p>" +
                "<p style=\"font-size: 14px;\">Thank you for registering with us! We are excited to have you on board. Please follow the link below to complete your registration and activate your account:</p>" +
                "<p style=\"font-size: 14px; color: #555;\">Here is your verification token:</p>" +
                "<p style=\"font-size: 18px; color: #000; font-weight: bold;\">" + verificationToken + "</p>" +
                "<p style=\"text-align: center; margin: 20px 0;\">" +
                "<a href=\"" + clientUrl + "\" style=\"display: inline-block; padding: 10px 20px; font-size: 16px; color: #fff; background-color: #007BFF; text-decoration: none; border-radius: 5px;\">Verify Your Email</a>" +
                "</p>" +
                "<p style=\"font-size: 14px;\">If you have any questions, feel free to reach out to us.</p>" +
                "<p style=\"font-size: 14px;\">Thank you,<br><strong>Team</strong></p>" +
                "</div>";

        MailRequest request = MailRequest.builder()
                .from(mailFrom)
                .senderName(senderName)
                .to(user.getEmail())
                .subject(subject)
                .text(mailContent)
                .build();

        sendEmail(request);
    }

    @Override
    public void sendTokenForPasswordRequest(String url, User user) {
        String subject = "Password Reset Request";
        String mailContent = "<p>Hi " + user.getUsername() + ",</p>" +
                "<p>You recently requested to reset your password. Please, follow the link below to complete the process:</p>" +
                "<a href=\"" + url + "\">Reset Password</a>" +
                "<p>Thank you,<br>Team</p>";

        MailRequest request = MailRequest.builder()
                .from(mailFrom)
                .senderName(senderName)
                .to(user.getEmail())
                .subject(subject)
                .text(mailContent)
                .build();

        sendEmail(request);
    }

    @Override
    public void welcomeMessage(User user) {
        String subject = "Welcome to the iDonate Platform!";
        String mailContent = "<p>Hi " + user.getUsername() + ",</p>" +
                "<p>Thank you for joining our platform. We’re excited to have you on board.</p>" +
                "<p>Explore our features and let us know if you have any questions.</p>" +
                "<p>Best regards,<br>Team</p>";

        MailRequest request = MailRequest.builder()
                .from(mailFrom)
                .senderName(senderName)
                .to(user.getEmail())
                .subject(subject)
                .text(mailContent)
                .build();

        sendEmail(request);
    }

    @Override
    public void welcomeOrganization(Organization organization) {
        String subject = "Welcome to the Fundraising Platform!";
        String mailContent = "<p>Dear " + organization.getName() + ",</p>" +
                "<p>Thank you for registering with us. We’re thrilled to support your fundraising efforts.</p>" +
                "<p>Get started by setting up your first campaign.</p>" +
                "<p>Best regards,<br>Team</p>";

        MailRequest request = MailRequest.builder()
                .from(mailFrom)
                .senderName(senderName)
                .to(organization.getEmail())
                .subject(subject)
                .text(mailContent)
                .build();

        sendEmail(request);
    }

    @Override
    public void sendDonationReceipt(User user, Receipt receipt) {
        String subject = "Donation Receipt";
        String mailContent = "<p>Hi " + user.getUsername() + ",</p>" +
                "<p>Thank you for your generous donation. Here are the details:</p>" +
                "<p>Receipt ID: " + receipt.getReceiptId() + "<br>" +
                "Amount: " + receipt.getAmount()+ "<br>" +
                "Date: " + receipt.getDate() + "<br>" +
                "Campaign: " + receipt.getEvent().getName() + "</p>" +
                "<p>We truly appreciate your support!</p>" +
                "<p>Best regards,<br>Team</p>";

        MailRequest request = MailRequest.builder()
                .from(mailFrom)
                .senderName(senderName)
                .to(user.getEmail())
                .subject(subject)
                .text(mailContent)
                .build();

        sendEmail(request);
    }

    @Override
    public void thankYouEmail(User user, String type) {
        String subject = "Thank You for Your Support!";
        String mailContent = "<p>Hi " + user.getUsername() + ",</p>" +
                "<p>We appreciate your " + type + ". Your contribution helps us make a difference.</p>" +
                "<p>Thank you for being a part of our journey.</p>" +
                "<p>Best regards,<br>Team</p>";

        MailRequest request = MailRequest.builder()
                .from(mailFrom)
                .senderName(senderName)
                .to(user.getEmail())
                .subject(subject)
                .text(mailContent)
                .build();

        sendEmail(request);
    }

    @Override
    public void accountNotification(User user, String relatedUpdate) {
        String subject = "Account Update Notification";
        String mailContent = "<p>Hi " + user.getUsername() + ",</p>" +
                "<p>Your account has been updated:</p>" +
                "<p><b>" + relatedUpdate + "</b></p>" +
                "<p>If you did not request this update, please contact us immediately.</p>" +
                "<p>Best regards,<br>Team</p>";

        MailRequest request = MailRequest.builder()
                .from(mailFrom)
                .senderName(senderName)
                .to(user.getEmail())
                .subject(subject)
                .text(mailContent)
                .build();

        sendEmail(request);
    }
}

