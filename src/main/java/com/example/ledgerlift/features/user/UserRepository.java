package com.example.ledgerlift.features.user;

import com.example.ledgerlift.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

}
