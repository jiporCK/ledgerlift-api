package com.example.ledgerlift.features.media;

import com.example.ledgerlift.base.BasedMessage;
import com.example.ledgerlift.features.media.dto.MediaResponse;
import com.example.ledgerlift.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@Slf4j
public class MediaServiceImpl implements MediaService{

    @Value("${media.server-path}")
    private String serverPath;

    @Value("${media.base-uri}")
    private String baseUri;

    @Override
    public MediaResponse uploadSingle(MultipartFile file, String folderName) {

        String newMediaName = Utils.generateUuid();
        newMediaName += Utils.extractExtension(file.getOriginalFilename());

        Path directoryPath = Paths.get(serverPath + folderName);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectory(directoryPath);
            } catch (IOException e) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Could not create directory", e
                );
            }
        }

        Path path = directoryPath.resolve(newMediaName);

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not copy file", e
            );
        }



        return MediaResponse.builder()
                .name(newMediaName)
                .contentType(file.getContentType())
                .extension(Utils.extractExtension(file.getOriginalFilename()))
                .size(file.getSize())
                .uri(String.format("%s%s/%s", baseUri, folderName, newMediaName))
                .build();

    }

    @Override
    public List<MediaResponse> uploadMultiple(List<MultipartFile> files, String s) {

        for (MultipartFile file : files) {

            String newMediaName = Utils.generateUuid();
            newMediaName += Utils.extractExtension(file.getOriginalFilename());

        }

        return List.of();
    }

    @Override
    public BasedMessage deleteMediaByName(String mediaName) {

        Path path = Paths.get(serverPath + mediaName);

        try {
            if (Files.deleteIfExists(path)) {
                return new BasedMessage("Media has been deleted");
            } throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Media has not been found");
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Media path %s cannot be deleted", e.getLocalizedMessage()));
        }

    }

}
