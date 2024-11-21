package com.example.ledgerlift.features.mail;

import com.example.ledgerlift.base.BasedMessage;
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

    @Override
    public void sendEmail(MailRequest request) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;

        try {
            messageHelper = new MimeMessageHelper(mimeMessage, true); // true enables multipart (HTML support)
            messageHelper.setFrom(mailFrom, senderName); // Correct sender email and name
            messageHelper.setTo(request.to()); // Recipient email
            messageHelper.setSubject(request.subject()); // Subject of the email
            messageHelper.setText(request.text(), true); // Enable HTML content

            mailSender.send(mimeMessage); // Send the email
            log.info("Email sent successfully to {}", request.to());
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Error sending email to {}: {}", request.to(), e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }


    @Override
    public BasedMessage sendTokenForEmailVerification(String url, User user, String verificationToken) {

        String subject = "Email Verification";
        String mailContent = "<p>Hi, " + user.getUsername() + ",</p>" +
                "<p>Thank you for registering with us. Please, follow the link below to complete your registration:</p>" +
                "<p>This is your token: " + verificationToken + "</p>" +
                "<a href=\"" + url + "/api/v1/users/verify-email?token=" + verificationToken + "\">Verify your email to activate your account</a>" +
                "<p>Thank you,<br>User Registration Portal Service</p>";

        MailRequest request = MailRequest.builder()
                .from(mailFrom)
                .senderName(senderName)
                .to(user.getEmail())
                .subject(subject)
                .text(mailContent)
                .build();

        sendEmail(request);

        return BasedMessage.builder()
                .message("Token has been sent")
                .build();
    }

    @Override
    public BasedMessage sendTokenForPasswordRequest(String url, User user) {

        String subject = "Password Reset Request Verification";
        String mailContent = "<p>Hi, " + user.getUsername() + ",</p>" +
                "<p><b>You recently requested to reset your password.</b> Please, follow the link below to complete the action:</p>" +
                "<a href=\"" + url + "\">Reset password</a>" +
                "<p>Thank you,<br>iData Service</p>";

        MailRequest request = MailRequest.builder()
                .from(mailFrom)
                .senderName(senderName)
                .to(user.getEmail())
                .subject(subject)
                .text(mailContent)
                .build();

        sendEmail(request);

        return BasedMessage.builder()
                .message("Token has been sent")
                .build();
    }

}
