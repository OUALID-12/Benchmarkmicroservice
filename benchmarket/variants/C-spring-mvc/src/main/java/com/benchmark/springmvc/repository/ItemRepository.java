package com.benchmark.springmvc.repository;

import com.benchmark.springmvc.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT i FROM Item i JOIN FETCH i.category WHERE i.category.id = :categoryId")
    Page<Item> findByCategoryIdWithJoin(Long categoryId, Pageable pageable);
}
