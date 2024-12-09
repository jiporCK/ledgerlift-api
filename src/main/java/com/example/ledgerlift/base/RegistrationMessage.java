package com.example.ledgerlift.base;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationMessage {

    private String message;

    private String token;

}
