package com.example.ledgerlift.features.testimony;

import com.example.ledgerlift.domain.Testimony;
import com.example.ledgerlift.features.media.dto.ImageRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface TestimonyService {

    void createTestimony(@Valid Testimony testimony);

    void uploadImage(String uuid, @Valid ImageRequest image);

    List<Testimony> getAll();

    void deleteByUuid(String uuid);

    void updateByUuid(String uuid, @Valid Testimony testimony);
}
