package com.example.ledgerlift.domain;

import com.example.ledgerlift.domain.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String username;

    private String password;

    private String phoneNumber;

    private String email;

    private String avatar;

    private Boolean isProfiledVisibility;

    private Boolean isEmailVerified;

    private Boolean isCAVerified;

    @OneToMany(mappedBy = "user")
    private List<Organization> organization;

    @ManyToMany
    private List<Role> roles;

}
