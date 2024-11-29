package com.example.ledgerlift.features.testimony;

import com.example.ledgerlift.domain.Testimony;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestimonyRepository extends JpaRepository<Testimony, Long> {


    Optional<Testimony> findByUuid(String uuid);
}
