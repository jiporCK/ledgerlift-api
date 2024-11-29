package com.example.ledgerlift.features.testimony;

import com.example.ledgerlift.domain.Testimony;
import com.example.ledgerlift.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public void uploadImage(String uuid, String image) {

        Testimony testimony = testimonyRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Testimony has not been found"
                        )
                );

        testimony.setImage(image);

        testimonyRepository.save(testimony);

    }
}
