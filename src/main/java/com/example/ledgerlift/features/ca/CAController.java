package com.example.ledgerlift.features.ca;

import com.example.ledgerlift.features.ca.dto.CAEnrollmentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ca-enrollment")
public class CAController {

    private final CAService caService;

    @PostMapping("/register-donor/{userUuid}")
    public ResponseEntity<?> registerUser(@PathVariable String userUuid,
                                          @Valid @RequestBody CAEnrollmentRequest request) throws Exception {
        try {
            caService.registerAndEnrollUser(userUuid,request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            "User registered and enrolled successfully"
                    );
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body(
                e.getMessage()
            );
        }
    }

    @PostMapping("/register-organization/{organizationUuid}")
    public ResponseEntity<?> registerOrganization(@PathVariable String organizationUuid,
                                                  @Valid @RequestBody CAEnrollmentRequest request) throws Exception {

        try {
            if (!"organization".equalsIgnoreCase(request.getType()) && !"admin".equalsIgnoreCase(request.getType())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid type. Only 'organization' or 'admin' is allowed.");
            }
            caService.registerAndEnrollUser(organizationUuid, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Organization registered and enrolled successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }

    }

}
