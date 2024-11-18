package com.example.ledgerlift.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "courses")
public class Cause {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    private String name;

    private String description;

    private Double goalAmount;

    private Date startDate;

    private Date endDate;

    private Boolean isCompleted;

    @ManyToOne
    private Organization organization;

    @ManyToOne
    private Category category;

}
