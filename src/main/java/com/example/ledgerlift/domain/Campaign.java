package com.example.ledgerlift.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "courses")
public class Campaign{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    private String description;

    private Double goalAmount;

    private Boolean isCompleted;

    @ManyToOne
    private Organization organization;

    @ManyToOne
    private Category category;

}
