package com.example.ledgerlift.features.cause;

import com.example.ledgerlift.domain.Cause;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CauseRepository extends JpaRepository<Cause, Long> {

    Optional<Cause> findByUuid(String uuid);

}
