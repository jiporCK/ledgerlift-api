package com.example.ledgerlift.init;

import com.example.ledgerlift.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String donor);
}
