package com.benchmark.springmvc.service;

import com.benchmark.springmvc.dto.CategoryDTO;
import com.benchmark.springmvc.entity.Category;
import com.benchmark.springmvc.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<CategoryDTO> findById(Long id) {
        return categoryRepository.findById(id).map(this::toDTO);
    }

    public CategoryDTO save(CategoryDTO categoryDTO) {
        Category category = toEntity(categoryDTO);
        Category saved = categoryRepository.save(category);
        return toDTO(saved);
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }

    private CategoryDTO toDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getCode(), category.getName());
    }

    private Category toEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setCode(dto.getCode());
        category.setName(dto.getName());
        return category;
    }
}
