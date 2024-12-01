package com.example.ledgerlift.domain;

import com.example.ledgerlift.domain.audit.Auditable;
import com.example.ledgerlift.features.mail.verificationToken.VerificationToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;

    @NonNull
    @Column(nullable = false)
    private String password;

    private String phoneNumber;

    @NonNull
    @Column(unique = true, nullable = false)
    private String email;

    private String avatar;

    private Boolean isProfiledVisibility;

    private Boolean isEmailVerified;

    private Boolean isAccountVerified;

    private Boolean isAnonymous;

    private LocalDateTime lastLoginAt;

    /**
     * Indicates whether the user's account is expired.
     */
    private boolean isAccountNonExpired;

    /**
     * Indicates whether the user's account is locked.
     */
    private boolean isAccountNonLocked;

    /**
     * Indicates whether the user's credentials are expired.
     */
    private boolean isCredentialsNonExpired;

    /**
     * Indicates whether the user's account is bloStringcked.
     */
    @Column(nullable = false)
    private Boolean isBlocked;

    /**
     * Indicates whether the user's account is deleted.
     */
    private boolean isDeleted;

    @OneToMany(mappedBy = "user")
    private List<Organization> organizations;

    @ManyToMany
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VerificationToken> tokens;

    @OneToMany(mappedBy = "user")
    private List<Receipt> receipts;

}
