package com.example.ledgerlift.features.media;

import com.example.ledgerlift.domain.Event;
import com.example.ledgerlift.domain.Media;
import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.event.EventRepository;
import com.example.ledgerlift.features.media.dto.MediaResponse;
import com.example.ledgerlift.features.user.UserRepository;
import com.example.ledgerlift.mapper.MediaMapper;
import com.example.ledgerlift.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService{

    private final EventRepository eventRepository;
    private final MediaMapper mediaMapper;
    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;

    @Value("${media.server-path}")
    private String serverPath;

    @Value("${media.base-uri}")
    private String baseUri;

    @Override
    public List<MediaResponse> uploadMultiple(String eventUuid, List<MultipartFile> files, String s) {

        Event event = eventRepository.findByUuid(eventUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Event has not been found"
                        )
                );

        List<MediaResponse> mediaResponses = new ArrayList<>();

        files.forEach(file -> {
            MediaResponse mediaResponse = this.uploadSingle(file, s);

            mediaResponses.add(mediaResponse);
        });

        List<Media> media = mediaMapper.fromMediaResponseList(mediaResponses);
        media.forEach(md -> {
            md.setEvent(event);
            mediaRepository.save(md);
        });

        event.setMedias(media);
        eventRepository.save(event);

        return mediaResponses;

    }

    @Override
    public List<MediaResponse> loadAllMedias() {

        Path path = Paths.get(serverPath + "/");

        try {
            List<MediaResponse> mediaResponses = new ArrayList<>();

            Files.list(path).forEach(mediaPath -> {
                try {
                    String mediaName = mediaPath.getFileName().toString();
                    Resource resource = new UrlResource(mediaPath.toUri());
                    if (resource.exists()) {
                        MediaResponse mediaResponse = MediaResponse.builder()
                                .name(mediaName)
                                .contentType(Files.probeContentType(mediaPath))
                                .extension(Utils.extractExtension(mediaName))
                                .size(resource.contentLength())
                                .uri(String.format("%s/%s", baseUri, mediaName))
                                .build();
                        mediaResponses.add(mediaResponse);
                    }
                } catch (IOException e) {
                    log.error("Error loading media: {}", e.getMessage());
                }
            });

            return mediaResponses;

        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Error loading media files from: %s", e.getMessage())
            );
        }

    }

    @Override
    public List<MediaResponse> getMediaByEvent(String uuid) {

        Event event = eventRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Event has not been found"
                        )
                );

        List<Media> media = event.getMedias();

        return mediaMapper.toMediaResponseList(media);
    }

    @Override
    public MediaResponse uploadSingle(MultipartFile file, String folderName) {

        String newMediaName = Utils.generateUuid() + Utils.extractExtension(file.getOriginalFilename());

        // Construct directory path
        Path directoryPath = Paths.get(serverPath, folderName);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);  // Create parent directories if necessary
            } catch (IOException e) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Could not create directory", e
                );
            }
        }

        // Resolve file path
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
    public void deleteMediaByName(String mediaName) {

        // Correct path construction
        Path path = Paths.get(serverPath, mediaName);
        log.info("Path: {}", path);

        try {
            Files.deleteIfExists(path);
            if (mediaRepository.existsByName(mediaName)) {
                Media media = mediaRepository.findByName(mediaName)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Media does not exist"));

                log.info("Media name: {}", media.getName());
                mediaRepository.delete(media);

                if (userRepository.existsByAvatar(mediaName)) {
                    User user = userRepository.findByAvatar(mediaName)
                            .orElseThrow(
                                    () -> new ResponseStatusException(
                                            HttpStatus.NOT_FOUND,
                                            "User with avatar: [" + mediaName + "] has not been found"
                                    )
                            );

                    user.setAvatar(null);
                    userRepository.save(user);
                }

            }
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Media path '%s' cannot be deleted", e.getLocalizedMessage()), e
            );
        }
    }

    @Override
    public MediaResponse getMediaByName(String mediaName) {
        // Construct file path
        Path path = Paths.get(serverPath, mediaName);

        try {
            Resource resource = new UrlResource(path.toUri());
            log.info("Load resource: {}", resource.getFilename());

            if (!resource.exists()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Media not found");
            }

            return MediaResponse.builder()
                    .name(mediaName)
                    .contentType(Files.probeContentType(path))
                    .extension(Utils.extractExtension(mediaName))
                    .size(resource.contentLength())
                    .uri(String.format("%s%s", baseUri, mediaName))
                    .build();
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getLocalizedMessage(), e
            );
        }
    }
}
