package com.benchmark.springmvc.controller;

import com.benchmark.springmvc.dto.CategoryDTO;
import com.benchmark.springmvc.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        logger.info("GET /categories?page={}&size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryDTO> categories = categoryService.findAll(pageable);

        return ResponseEntity.ok(categories.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        logger.info("GET /categories/{}", id);
        CategoryDTO category = categoryService.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));

        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        logger.info("POST /categories");
        CategoryDTO saved = categoryService.save(categoryDTO);

        return ResponseEntity.created(URI.create("/categories/" + saved.getId()))
                      .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        logger.info("PUT /categories/{}", id);
        if (!categoryService.existsById(id)) {
            throw new RuntimeException("Category not found");
        }

        categoryDTO.setId(id);
        CategoryDTO updated = categoryService.save(categoryDTO);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        logger.info("DELETE /categories/{}", id);
        if (!categoryService.existsById(id)) {
            throw new RuntimeException("Category not found");
        }

        categoryService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
