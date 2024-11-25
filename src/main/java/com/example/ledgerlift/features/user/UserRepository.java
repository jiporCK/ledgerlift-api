package com.example.ledgerlift.features.user;

import com.example.ledgerlift.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByUuid(String uuid);

    boolean existsByUuid(String uuid);

    boolean existsByUsername(String username);

    @Modifying
    @Query("UPDATE User u SET u.isBlocked = TRUE WHERE u.uuid = :uuid")
    void blockByUuid(@Param("uuid") String uuid);

    @Modifying
    @Query("UPDATE User u SET u.isBlocked = FALSE WHERE u.uuid = :uuid")
    void unBlockByUuid(@Param("uuid") String uuid);

}
