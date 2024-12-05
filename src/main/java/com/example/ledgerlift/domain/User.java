package com.example.ledgerlift.domain;

import com.example.ledgerlift.domain.audit.Auditable;
import com.example.ledgerlift.features.mail.verificationToken.VerificationToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.checkerframework.checker.formatter.qual.Format;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateOfBirth;

    private Boolean isProfiledVisibility;

    private Boolean isEmailVerified;

    private Boolean isAccountVerified;

    private Boolean isAnonymous;

    private LocalDateTime lastLoginAt;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    @Column(nullable = false)
    private Boolean isBlocked;

    private boolean isDeleted;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Media media;

    @OneToMany(mappedBy = "user")
    private List<Organization> organizations;

    @ManyToMany
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VerificationToken> tokens;

    @OneToMany(mappedBy = "user")
    private List<Receipt> receipts;

}
