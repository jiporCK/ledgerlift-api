package com.example.ledgerlift.features.catetory;

import com.example.ledgerlift.features.catetory.dto.CategoryRequest;
import com.example.ledgerlift.features.catetory.dto.CategoryResponse;

public interface CategoryService {

    void createCategory(CategoryRequest request);

    CategoryResponse getCategoryByName(String categoryName);

}
