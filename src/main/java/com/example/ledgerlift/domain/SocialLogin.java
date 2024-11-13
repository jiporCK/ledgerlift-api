package com.example.ledgerlift.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents a social login entity for a user.
 * This entity maps to the table "dt_social_logins".
 * It includes the social login provider and a reference to the associated user.
 */
@Entity
@Table(name = "social_logins")
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
public class SocialLogin {

    /**
     * The unique identifier for the social login record.
     * This value is auto-generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the social login provider (e.g., Google, Facebook).
     */
    private String provider;

    /**
     * The user associated with this social login.
     * This is a one-to-one relationship with the User entity.
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
