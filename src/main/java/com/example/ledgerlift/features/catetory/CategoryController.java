package com.example.ledgerlift.features.catetory;

import com.example.ledgerlift.features.catetory.dto.CategoryRequest;
import com.example.ledgerlift.features.catetory.dto.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createCategory(@Valid @RequestBody CategoryRequest request) {

        categoryService.createCategory(request);

    }

    @GetMapping
    public CategoryResponse getCategory(@RequestParam String name) {

        return categoryService.getCategoryByName(name);

    }

}
