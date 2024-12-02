package com.example.ledgerlift.features.media;

import com.example.ledgerlift.base.BasedMessage;
import com.example.ledgerlift.features.media.dto.MediaResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {

    MediaResponse uploadSingle(MultipartFile file, String s);

    List<MediaResponse> uploadMultiple(String eventUuid, List<MultipartFile> files, String s);

    BasedMessage deleteMediaByName(String mediaName);

    List<MediaResponse> loadAllMedias();

    MediaResponse getMediaByName(String mediaName);

    List<MediaResponse> getMediaByEvent(String uuid);
}
