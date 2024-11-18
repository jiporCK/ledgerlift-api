package com.example.ledgerlift.features.media;

import com.example.ledgerlift.features.media.dto.MediaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class MediaServiceImpl implements MediaService{
    @Override
    public MediaResponse uploadSingle(MultipartFile file, String s) {
        return null;
    }
}
