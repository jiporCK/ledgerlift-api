package com.example.ledgerlift.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    private String image;

    private String name;

    private String description;

    private BigDecimal goalAmount;

    private BigDecimal currentAmount;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean isCompleted;

    @ManyToOne
    private Organization organization;

    @ManyToOne
    private Category category;

}
