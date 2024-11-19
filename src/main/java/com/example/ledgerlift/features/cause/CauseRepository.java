package com.example.ledgerlift.features.cause;

import com.example.ledgerlift.domain.Cause;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CauseRepository extends JpaRepository<Cause, Long> {
}
