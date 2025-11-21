package com.expense_tracker.repository;

import com.expense_tracker.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByUserIdAndMonthAndYearAndCategoryId(
            Long userid,
            Integer month,
            Integer year,
            Long categoryId
    );

    List<Budget> findByUserId(Long userId);

    List<Budget> findByUserIdAndMonthAndYear(
            Long userId,
            Integer month,
            Integer year
    );
}