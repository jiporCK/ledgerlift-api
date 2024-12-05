package com.example.ledgerlift.features.sociallogins.sessionpayload;

import com.example.ledgerlift.features.sociallogins.sessionpayload.google.UserGoogle;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GooglePayload {

    private UserGoogle user;

    private LocalDateTime expires;
}
