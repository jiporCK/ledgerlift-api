package com.example.ledgerlift.features.organization;

import com.example.ledgerlift.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findByUuid(String organizationUuid);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE Organization o SET o.isLocked = TRUE WHERE o.uuid = :uuid")
    void lockByUuid(@Param("uuid")String uuid);
}
