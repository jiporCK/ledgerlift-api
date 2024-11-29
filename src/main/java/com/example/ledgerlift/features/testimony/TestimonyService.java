package com.example.ledgerlift.features.testimony;

import com.example.ledgerlift.domain.Testimony;
import jakarta.validation.Valid;

public interface TestimonyService {
    void createTestimony(@Valid Testimony testimony);

    void uploadImage(String uuid, @Valid String image);
}
