package com.example.ledgerlift.features.catetory;

import com.example.ledgerlift.base.BasedMessage;
import com.example.ledgerlift.domain.Category;
import com.example.ledgerlift.features.catetory.dto.CategoryRequest;
import com.example.ledgerlift.features.catetory.dto.CategoryResponse;
import com.example.ledgerlift.mapper.CategoryMapper;
import com.example.ledgerlift.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {

        Category category = mapper.fromCategoryRequest(request);

        if (repository.existsByName(request.name())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Category is already exists"
            );
        }

        category.setUuid(Utils.generateUuid());

        repository.save(category);

        return mapper.toCategoryResponse(category);

    }

    @Override
    public CategoryResponse getCategoryByName(String categoryName) {

        Category category = repository.findByNameContainsIgnoreCase(categoryName);

        return mapper.toCategoryResponse(category);
    }

    @Override
    public void updateCategoryByUuid(String uuid, CategoryRequest request) {

        Category category = repository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")
                );

        category.setName(request.name());
        category.setDescription(request.description());

        repository.save(category);

    }

    @Override
    public void deleteByUuid(String uuid) {

        Category category = categoryRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")
                );

        repository.delete(category);

    }

}
