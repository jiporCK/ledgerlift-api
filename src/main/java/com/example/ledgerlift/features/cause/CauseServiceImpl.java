package com.example.ledgerlift.features.cause;

import com.example.ledgerlift.domain.Category;
import com.example.ledgerlift.domain.Cause;
import com.example.ledgerlift.domain.Organization;
import com.example.ledgerlift.features.catetory.CategoryRepository;
import com.example.ledgerlift.features.cause.dto.CauseRequest;
import com.example.ledgerlift.features.cause.dto.CauseResponse;
import com.example.ledgerlift.features.organization.OrganizationRepository;
import com.example.ledgerlift.mapper.CauseMapper;
import com.example.ledgerlift.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CauseServiceImpl implements CauseService {

    private final CauseRepository causeRepository;
    private final OrganizationRepository organizationRepository;
    private final CategoryRepository categoryRepository;

    private final CauseMapper causeMapper;

    @Override
    public void createCause(String organizationUuid, String categoryUuid, CauseRequest causeRequest) {

        Organization organization = organizationRepository.findByUuid(organizationUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Organization has not been found!"
                        )
                );

        Category category = categoryRepository.findByUuid(categoryUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.CREATED,
                                "Category has not been found!"
                        )
                );

        Cause cause = causeMapper.fromCauseCreateRequest(causeRequest);
        cause.setUuid(Utils.generateUuid());
        cause.setStartDate(LocalDateTime.now());
        cause.setEndDate(LocalDateTime.now().plusMonths(1));
        cause.setIsCompleted(false);
        cause.setOrganization(organization);
        cause.setCategory(category);

        categoryRepository.save(category);

    }

    @Override
    public CauseResponse getCauseByUuid(String uuid) {
        return null;
    }

    @Override
    public List<CauseResponse> getAllCauses() {
        return List.of();
    }

    @Override
    public void deleteCauseByUuid(String uuid) {

    }

    @Override
    public void updateCauseByUuid(String uuid, CauseRequest causeRequest) {

    }
}
