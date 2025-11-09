package com.benchmark.springmvc.controller;

import com.benchmark.springmvc.dto.ItemDTO;
import com.benchmark.springmvc.service.CategoryService;
import com.benchmark.springmvc.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories/{categoryId}/items")
public class CategoryItemController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryItemController.class);

    private final ItemService itemService;
    private final CategoryService categoryService;

    public CategoryItemController(ItemService itemService, CategoryService categoryService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getCategoryItems(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        logger.info("GET /categories/{}/items?page={}&size={}", categoryId, page, size);

        // Verify category exists
        if (!categoryService.existsById(categoryId)) {
            throw new RuntimeException("Category not found");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ItemDTO> items = itemService.findByCategoryId(categoryId, pageable);

        return ResponseEntity.ok(items.getContent());
    }
}
