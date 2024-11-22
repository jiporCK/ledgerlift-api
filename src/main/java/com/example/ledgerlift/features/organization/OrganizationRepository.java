package com.example.ledgerlift.features.organization;

import com.example.ledgerlift.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findByUuid(String organizationUuid);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

}
