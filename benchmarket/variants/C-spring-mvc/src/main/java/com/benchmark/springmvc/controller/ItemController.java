package com.benchmark.springmvc.controller;

import com.benchmark.springmvc.dto.ItemDTO;
import com.benchmark.springmvc.service.ItemService;
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
@RequestMapping("/items")
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long categoryId) {
        logger.info("GET /items?page={}&size={}&categoryId={}", page, size, categoryId);

        Pageable pageable = PageRequest.of(page, size);
        Page<ItemDTO> items;

        if (categoryId != null) {
            items = itemService.findByCategoryId(categoryId, pageable);
        } else {
            items = itemService.findAll(pageable);
        }

        return ResponseEntity.ok(items.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItem(@PathVariable Long id) {
        logger.info("GET /items/{}", id);
        ItemDTO item = itemService.findById(id)
            .orElseThrow(() -> new RuntimeException("Item not found"));

        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody ItemDTO itemDTO) {
        logger.info("POST /items");
        ItemDTO saved = itemService.save(itemDTO);

        return ResponseEntity.created(URI.create("/items/" + saved.getId()))
                      .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Long id, @Valid @RequestBody ItemDTO itemDTO) {
        logger.info("PUT /items/{}", id);
        if (!itemService.existsById(id)) {
            throw new RuntimeException("Item not found");
        }

        itemDTO.setId(id);
        ItemDTO updated = itemService.save(itemDTO);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        logger.info("DELETE /items/{}", id);
        if (!itemService.existsById(id)) {
            throw new RuntimeException("Item not found");
        }

        itemService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
