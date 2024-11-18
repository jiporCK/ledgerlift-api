package com.example.ledgerlift.domain;


import com.example.ledgerlift.domain.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "organizations")
public class Organization extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String phone;

    private String address;

    private String description;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "organization")
    private List<Cause> causes;

}
