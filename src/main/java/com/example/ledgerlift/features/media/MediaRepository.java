package com.example.ledgerlift.features.media;

import com.example.ledgerlift.domain.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {


    Optional<Media> findByName(String name);

    boolean existsByName(String name);
}
