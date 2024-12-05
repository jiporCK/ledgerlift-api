package com.example.ledgerlift.features.sociallogins.sessionpayload.google;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserGoogle {

    private String id;

    private String name;

    @NonNull
    @Email
    private String email;

    private String image;

}
