package com.example.ledgerlift.features.testimony;

import com.example.ledgerlift.base.BasedMessage;
import com.example.ledgerlift.domain.Testimony;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/testimonies")
@RequiredArgsConstructor
public class TestimonyController {

    private final TestimonyService testimonyService;

    @PostMapping
    public BasedMessage createTestimony(@Valid @RequestBody Testimony testimony) {

        testimonyService.createTestimony(testimony);

        return new BasedMessage("Testimony created");

    }

    @PutMapping("/{uuid}/upload-image")
    public BasedMessage uploadImage(@PathVariable String uuid,
                                    @Valid @RequestBody String image) {

        testimonyService.uploadImage(uuid, image);

        return new BasedMessage("Test image uploaded");

    }

}
