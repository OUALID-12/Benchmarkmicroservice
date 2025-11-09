package com.benchmark.jaxrs.repository;

import com.benchmark.jaxrs.entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Category.class, id));
    }

    public Page<Category> findAll(Pageable pageable) {
        TypedQuery<Category> query = entityManager.createQuery("SELECT c FROM Category c", Category.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        TypedQuery<Long> countQuery = entityManager.createQuery("SELECT COUNT(c) FROM Category c", Long.class);
        Long total = countQuery.getSingleResult();

        List<Category> results = query.getResultList();
        return new PageImpl<>(results, pageable, total);
    }

    public Category save(Category category) {
        if (category.getId() == null) {
            entityManager.persist(category);
            return category;
        } else {
            return entityManager.merge(category);
        }
    }

    public void deleteById(Long id) {
        Category category = entityManager.find(Category.class, id);
        if (category != null) {
            entityManager.remove(category);
        }
    }

    public boolean existsById(Long id) {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(c) FROM Category c WHERE c.id = :id", Long.class);
        query.setParameter("id", id);
        return query.getSingleResult() > 0;
    }
}
