package com.example.ledgerlift.features.media;

import com.example.ledgerlift.base.BasedMessage;
import com.example.ledgerlift.features.media.dto.MediaResponse;
import jakarta.validation.Valid;
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/upload-multiple/{eventUuid}")
    List<MediaResponse> uploadMultiple(@PathVariable String eventUuid,
                                       @Valid @RequestBody List<MultipartFile> files) {

        return mediaService.uploadMultiple(eventUuid, files, "");

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{mediaName}")
    public BasedMessage deleteMediaByName(@PathVariable String mediaName) {

        mediaService.deleteMediaByName(mediaName);

        return new BasedMessage("Media has been deleted");

    }

    @GetMapping
    public List<MediaResponse> getMediaByEvent() {

        return mediaService.loadAllMedias();

    }

    @GetMapping("/get-by-name/{mediaName}")
    public MediaResponse getMediaByName(@PathVariable String mediaName) {

        return mediaService.getMediaByName(mediaName);

    }

    @GetMapping("/get-media-by-event/{uuid}")
    public List<MediaResponse> getMediaByEvent(@PathVariable String uuid) {

        return mediaService.getMediaByEvent(uuid);

    }

}
