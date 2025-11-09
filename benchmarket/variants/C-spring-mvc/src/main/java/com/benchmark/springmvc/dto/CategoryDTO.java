package com.benchmark.springmvc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDTO {

    private Long id;

    @NotBlank
    @Size(max = 32)
    private String code;

    @NotBlank
    @Size(max = 128)
    private String name;

    // Constructors
    public CategoryDTO() {}

    public CategoryDTO(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
