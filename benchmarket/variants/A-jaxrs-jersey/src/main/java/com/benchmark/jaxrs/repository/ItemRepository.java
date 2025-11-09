package com.benchmark.jaxrs.repository;

import com.benchmark.jaxrs.entity.Item;
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
public class ItemRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Item.class, id));
    }

    public Page<Item> findAll(Pageable pageable) {
        TypedQuery<Item> query = entityManager.createQuery("SELECT i FROM Item i", Item.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        TypedQuery<Long> countQuery = entityManager.createQuery("SELECT COUNT(i) FROM Item i", Long.class);
        Long total = countQuery.getSingleResult();

        List<Item> results = query.getResultList();
        return new PageImpl<>(results, pageable, total);
    }

    public Page<Item> findByCategoryId(Long categoryId, Pageable pageable) {
        TypedQuery<Item> query = entityManager.createQuery(
            "SELECT i FROM Item i WHERE i.category.id = :categoryId", Item.class);
        query.setParameter("categoryId", categoryId);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        TypedQuery<Long> countQuery = entityManager.createQuery(
            "SELECT COUNT(i) FROM Item i WHERE i.category.id = :categoryId", Long.class);
        countQuery.setParameter("categoryId", categoryId);
        Long total = countQuery.getSingleResult();

        List<Item> results = query.getResultList();
        return new PageImpl<>(results, pageable, total);
    }

    public Page<Item> findByCategoryIdWithJoin(Long categoryId, Pageable pageable) {
        TypedQuery<Item> query = entityManager.createQuery(
            "SELECT i FROM Item i JOIN FETCH i.category WHERE i.category.id = :categoryId", Item.class);
        query.setParameter("categoryId", categoryId);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        TypedQuery<Long> countQuery = entityManager.createQuery(
            "SELECT COUNT(i) FROM Item i WHERE i.category.id = :categoryId", Long.class);
        countQuery.setParameter("categoryId", categoryId);
        Long total = countQuery.getSingleResult();

        List<Item> results = query.getResultList();
        return new PageImpl<>(results, pageable, total);
    }

    public Item save(Item item) {
        if (item.getId() == null) {
            entityManager.persist(item);
            return item;
        } else {
            return entityManager.merge(item);
        }
    }

    public void deleteById(Long id) {
        Item item = entityManager.find(Item.class, id);
        if (item != null) {
            entityManager.remove(item);
        }
    }

    public boolean existsById(Long id) {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(i) FROM Item i WHERE i.id = :id", Long.class);
        query.setParameter("id", id);
        return query.getSingleResult() > 0;
    }
}
