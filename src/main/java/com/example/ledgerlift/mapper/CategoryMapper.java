package com.example.ledgerlift.mapper;

import com.example.ledgerlift.domain.Category;
import com.example.ledgerlift.features.catetory.dto.CategoryRequest;
import com.example.ledgerlift.features.catetory.dto.CategoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category fromCategoryRequest(CategoryRequest categoryRequest);

    CategoryResponse toCategoryResponse(Category category);

}
