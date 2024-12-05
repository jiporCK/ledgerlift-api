package com.example.ledgerlift.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "medias")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String contentType;

    private String extension;

    private String uri;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long size;

    @ManyToOne
    private Event event;

    @OneToOne
    private User user;

}
