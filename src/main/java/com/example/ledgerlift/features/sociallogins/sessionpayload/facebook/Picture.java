package com.example.ledgerlift.features.sociallogins.sessionpayload.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Picture {

    private PictureData data;

}
