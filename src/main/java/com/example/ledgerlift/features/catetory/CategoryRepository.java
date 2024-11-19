package com.example.ledgerlift.features.catetory;

import com.example.ledgerlift.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByUuid(String uuid);

    boolean existsByName(String name);

    Optional<Category> findByNameContaining(String categoryName);
}
