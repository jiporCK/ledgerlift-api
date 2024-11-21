package com.example.ledgerlift.features.mail.verificationToken;

import com.example.ledgerlift.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    Optional<VerificationToken> findByUser(User user);

    Optional<VerificationToken> findByTokenAndType(String token, VerificationToken.TokenType tokenType);
}
