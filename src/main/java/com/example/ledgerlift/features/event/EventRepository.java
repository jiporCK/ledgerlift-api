package com.example.ledgerlift.features.event;

import com.example.ledgerlift.domain.Category;
import com.example.ledgerlift.domain.Event;
import com.example.ledgerlift.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByUuid(String uuid);

    List<Event> findAllByCategory(Category category);

    List<Event> findAllByOrganization(Organization organization);

}
