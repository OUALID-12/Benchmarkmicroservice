package com.benchmark.springdatarest.repository;

import com.benchmark.springdatarest.entity.Item;
import com.benchmark.springdatarest.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "items", collectionResourceRel = "items", itemResourceRel = "item")
public interface ItemRepository extends JpaRepository<Item, Long> {

    // Baseline (no join fetch) - pageable for REST HAL pagination
    @Query("SELECT i FROM Item i WHERE i.category.id = :categoryId")
    Page<Item> findByCategoryId(Long categoryId, Pageable pageable);

    // JOIN FETCH variant (optional). Note: for paging correctness, prefer @EntityGraph
    @EntityGraph(attributePaths = "category")
    @Query("SELECT i FROM Item i WHERE i.category.id = :categoryId")
    Page<Item> findByCategoryIdWithJoin(Long categoryId, Pageable pageable);

    // Reverse relation for /items/{id}/category
    @RestResource(path = "category", rel = "category")
    Category findCategoryById(Long id);
}
