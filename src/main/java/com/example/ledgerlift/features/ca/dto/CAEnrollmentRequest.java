package com.example.ledgerlift.features.ca.dto;

import jakarta.persistence.Column;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CAEnrollmentRequest {

    private String username;

    private String secret;

    private String affiliation; // org1.department1

    private String orgName;

    private String registrarUsername; // we can hide this in prod

    private String type ; // peer  | client | admin !

    @Builder.Default
    private Boolean genSecret = true;

}
