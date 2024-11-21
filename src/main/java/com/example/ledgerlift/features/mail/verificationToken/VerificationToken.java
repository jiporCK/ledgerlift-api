package com.example.ledgerlift.features.mail.verificationToken;

import com.example.ledgerlift.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "email_tokens")
@Entity
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    private String token;

    private Boolean isUsed;

    private Date expirationTime;
    private static final int EXPIRATION_TIME = 5;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType type;

    public enum TokenType {
        EMAIL_VERIFICATION,
        RESET_PASSWORD
    }

    public VerificationToken(String token, TokenType type) {
        super();
        this.createdAt = LocalDateTime.now();
        this.token = token;
        this.type = type;
        this.isUsed = false;
        this.expirationTime = this.getExpirationTime();
    }

    public Date getExpirationTime() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());

    }

    public boolean isExpired() {
        return new Date().after(this.expirationTime);
    }

}
