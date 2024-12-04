package com.example.ledgerlift.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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

    private String location;

    private BigDecimal currentRaised;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private Boolean isVisible;

    private Boolean isUrgent;

    @ManyToOne
    private Organization organization;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "event")
    private List<Receipt> receipts;

    @OneToMany(mappedBy = "event")
    private List<Media> medias;

}
