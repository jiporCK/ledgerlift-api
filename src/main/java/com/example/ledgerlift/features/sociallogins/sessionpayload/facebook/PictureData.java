package com.example.ledgerlift.features.sociallogins.sessionpayload.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PictureData {

    private String url;

    @JsonProperty("is_silhouette")
    private boolean isSilhouette;

}
