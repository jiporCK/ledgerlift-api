package com.example.ledgerlift.features.user;

import com.example.ledgerlift.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByUuid(String userUuid);

//    @Modifying
//    @Query("UPDATE User as u SET u.isDeleted = TRUE WHERE u.uuid = ?1")
//    void disableByUuid(String uuid);
//
//    @Modifying
//    @Query("UPDATE User as u SET u.isDeleted = FALSE WHERE u.uuid = ?1")
//    void enableByUuid(String uuid);

}
