package com.example.ledgerlift.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mapstruct.ap.internal.model.GeneratedType;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "testimonies")
public class Testimony {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    private String name;

    private String position;

    private String comment;

    private String image;

}
