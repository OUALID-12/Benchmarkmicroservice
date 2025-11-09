package com.benchmark.resteasy.service;

import com.benchmark.resteasy.dto.CategoryDTO;
import com.benchmark.resteasy.entity.Category;
import com.benchmark.resteasy.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<CategoryDTO> findAll(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(this::toDTO);
    }

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
