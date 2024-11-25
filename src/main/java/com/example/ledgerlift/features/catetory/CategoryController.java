package com.example.ledgerlift.features.catetory;

import com.example.ledgerlift.base.BasedMessage;
import com.example.ledgerlift.features.catetory.dto.CategoryRequest;
import com.example.ledgerlift.features.catetory.dto.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
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

    @PutMapping("/{uuid}")
    public BasedMessage updateCategory(@PathVariable String uuid,
                                       @Valid @RequestBody CategoryRequest request) {

        categoryService.updateCategoryByUuid(uuid, request);

        return BasedMessage.builder()
                .message("Category has been updated!")
                .build();

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{uuid}")
    public void deleteCategoryByUuid(@PathVariable String uuid) {

        categoryService.deleteByUuid(uuid);

    }

}
