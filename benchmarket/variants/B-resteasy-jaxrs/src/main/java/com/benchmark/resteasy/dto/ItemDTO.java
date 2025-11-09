package com.benchmark.resteasy.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ItemDTO {

    private Long id;

    @NotBlank
    @Size(max = 64)
    private String sku;

    @NotBlank
    @Size(max = 128)
    private String name;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal price;

    @Min(0)
    private Integer stock;

    @NotNull
    private Long categoryId;

    // Optional nested payload support: { "category": { "id": 1 } }
    public static class CategoryRef { private Long id; public Long getId(){return id;} public void setId(Long id){this.id=id;} }
    private CategoryRef category;

    // Optional heavy payload field for HEAVY-body scenario
    private String description;

    private String categoryCode;
    private String categoryName;

    // Constructors
    public ItemDTO() {}

    public ItemDTO(Long id, String sku, String name, BigDecimal price, Integer stock, Long categoryId) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public CategoryRef getCategory() { return category; }
    public void setCategory(CategoryRef category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}
