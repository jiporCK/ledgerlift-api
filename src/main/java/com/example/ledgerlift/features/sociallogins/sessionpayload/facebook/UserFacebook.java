package com.example.ledgerlift.features.sociallogins.sessionpayload.facebook;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserFacebook {

    private String id;

    private String name;

    @NonNull
    @Email
    private String email;

    private Picture picture;

}
