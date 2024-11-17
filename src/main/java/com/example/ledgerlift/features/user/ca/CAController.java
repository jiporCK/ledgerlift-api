package com.example.ledgerlift.features.user.ca;

import com.example.ledgerlift.features.user.ca.dto.CAEnrollmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/enrollment")
public class CAController {

    private final CAService caService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody CAEnrollmentRequest request) throws Exception {
        try {
            caService.registerAndEnrollUser(request);
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

}
