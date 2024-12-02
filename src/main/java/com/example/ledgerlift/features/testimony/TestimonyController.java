package com.example.ledgerlift.features.testimony;

import com.example.ledgerlift.base.BasedMessage;
import com.example.ledgerlift.domain.Testimony;
import com.example.ledgerlift.features.media.dto.ImageRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/testimonies")
@RequiredArgsConstructor
public class TestimonyController {

    private final TestimonyService testimonyService;

    @PreAuthorize("hasAnyAuthority('SCOPE_admin:read', 'SCOPE_admin:write')")
    @PostMapping
    public BasedMessage createTestimony(@Valid @RequestBody Testimony testimony) {

        testimonyService.createTestimony(testimony);

        return new BasedMessage("Testimony created");

    }

    @PreAuthorize("hasAnyAuthority('SCOPE_admin:read', 'SCOPE_admin:write')")
    @GetMapping
    public List<Testimony> getAllTestimonies() {

        return testimonyService.getAll();

    }

    @PreAuthorize("hasAnyAuthority('SCOPE_admin:read', 'SCOPE_admin:write')")
    @PutMapping("/{uuid}")
    public BasedMessage updateTestimony(@PathVariable String uuid,
                                        @Valid @RequestBody Testimony testimony) {

        testimonyService.updateByUuid(uuid, testimony);

        return new BasedMessage("Testimony updated!");

    }

    @PreAuthorize("hasAnyAuthority('SCOPE_admin:read', 'SCOPE_admin:write')")
    @DeleteMapping("/{uuid}")
    public BasedMessage deleteTestimony(@PathVariable String uuid) {

        testimonyService.deleteByUuid(uuid);

        return new BasedMessage("Testimony deleted!");

    }

    @PreAuthorize("hasAnyAuthority('SCOPE_admin:read', 'SCOPE_admin:write')")
    @PutMapping("/{uuid}/upload-image")
    public BasedMessage uploadImage(@PathVariable String uuid,
                                    @Valid @RequestBody ImageRequest image) {

        testimonyService.uploadImage(uuid, image);

        return new BasedMessage("Test image uploaded");

    }

}
