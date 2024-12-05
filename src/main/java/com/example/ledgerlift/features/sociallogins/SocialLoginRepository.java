package com.example.ledgerlift.features.sociallogins;

import com.example.ledgerlift.domain.SocialLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialLoginRepository extends JpaRepository<SocialLogin, Long> {
}
