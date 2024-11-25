package com.example.ledgerlift.features.catetory;

import com.example.ledgerlift.base.BasedMessage;
import com.example.ledgerlift.features.catetory.dto.CategoryRequest;
import com.example.ledgerlift.features.catetory.dto.CategoryResponse;
import jakarta.validation.Valid;

public interface CategoryService {

    void createCategory(CategoryRequest request);

    CategoryResponse getCategoryByName(String categoryName);

    void updateCategoryByUuid(String uuid, @Valid CategoryRequest request);

    void deleteByUuid(String uuid);
}
