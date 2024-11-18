package com.example.ledgerlift.features.media;

import com.example.ledgerlift.features.media.dto.MediaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/media")
public class MediaController {

    private final MediaService mediaService;

    @PostMapping(value = "/upload-single", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    MediaResponse uploadSingle(@RequestBody MultipartFile file) {
        return mediaService.uploadSingle(file, "");
    }

}
