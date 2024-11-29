package com.example.ledgerlift.features.media;

import com.example.ledgerlift.base.BasedMessage;
import com.example.ledgerlift.features.media.dto.MediaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/media")
public class MediaController {

    private final MediaService mediaService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/upload-single", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    MediaResponse uploadSingle(@RequestBody MultipartFile file) {
        return mediaService.uploadSingle(file, "");
    }

    @PostMapping("/{eventUuid}/upload-multiple")
    BasedMessage uploadMultiple(@PathVariable String eventUuid,
                                @RequestBody List<MultipartFile> files) {

        mediaService.uploadMultiple(eventUuid, files, "");

        return new BasedMessage("Successfully uploaded multiple files");

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{mediaName}")
    public BasedMessage deleteMediaByName(@PathVariable String mediaName) {

        return mediaService.deleteMediaByName(mediaName);

    }

}
