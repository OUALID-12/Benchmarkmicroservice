package com.benchmark.springdatarest.repository;

import com.benchmark.springdatarest.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "categories", collectionResourceRel = "categories", itemResourceRel = "category")
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
