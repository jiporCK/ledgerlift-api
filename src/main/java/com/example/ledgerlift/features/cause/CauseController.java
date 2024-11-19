package com.example.ledgerlift.features.cause;

import com.example.ledgerlift.features.cause.dto.CauseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/causes")
public class CauseController {

    private final CauseService causeService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{organizationUuid}/{categoryUuid}")
    public void createCause(@PathVariable String organizationUuid,
                            @PathVariable String categoryUuid,
                            @RequestBody CauseRequest request) {

        causeService.createCause(organizationUuid, categoryUuid, request);

    }

}
