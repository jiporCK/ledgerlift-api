package com.example.ledgerlift.features.testimony;

import com.example.ledgerlift.domain.Testimony;
import com.example.ledgerlift.features.media.dto.ImageRequest;
import com.example.ledgerlift.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestimonyServiceImpl implements TestimonyService {


    private final TestimonyRepository testimonyRepository;

    @Override
    public void createTestimony(Testimony testimony) {

        Testimony newTestimony = new Testimony();

        newTestimony.setUuid(Utils.generateUuid());
        newTestimony.setName(testimony.getName());
        newTestimony.setComment(testimony.getComment());
        newTestimony.setPosition(testimony.getPosition());

        testimonyRepository.save(newTestimony);

    }

    @Override
    public void uploadImage(String uuid, ImageRequest image) {

        Testimony testimony = testimonyRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Testimony has not been found"
                        )
                );

        testimony.setImage(image.image());

        testimonyRepository.save(testimony);

    }

    @Override
    public List<Testimony> getAll() {

        return testimonyRepository.findAll();

    }

    @Override
    public void deleteByUuid(String uuid) {
        Testimony testimony = testimonyRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Testimony has not been found!"
                        )
                );

        testimonyRepository.delete(testimony);

    }

    @Override
    public void updateByUuid(String uuid, Testimony testimony) {
        Testimony existTestimony = testimonyRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Testimony has not been found!"
                        )
                );

        existTestimony.setName(testimony.getName());
        existTestimony.setComment(testimony.getComment());
        existTestimony.setPosition(testimony.getPosition());

        testimonyRepository.save(existTestimony);
    }
}
