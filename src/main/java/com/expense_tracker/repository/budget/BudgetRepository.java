package com.expense_tracker.repository.budget;

import com.expense_tracker.model.budget.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("""
                SELECT b FROM Budget b
                WHERE b.month = :month AND b.year = :year
            """)
    List<Budget> findByMonthAndYear(int month, int year);


}