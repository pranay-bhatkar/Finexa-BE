package com.expense_tracker.repository;

import com.expense_tracker.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserIdOrUserIdIsNull(Long userId);
    boolean existsByNameAndUserId(String name, Long userId); // Check duplicates

    @Query("SELECT c FROM Category c  WHERE (c.userId = :userId OR c.userId IS NULL) AND c.deleted = false")
    List<Category> findActiveCategories(Long userId);

}