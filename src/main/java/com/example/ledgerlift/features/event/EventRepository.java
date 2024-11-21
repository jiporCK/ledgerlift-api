package com.example.ledgerlift.features.event;

import com.example.ledgerlift.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByUuid(String uuid);

}
