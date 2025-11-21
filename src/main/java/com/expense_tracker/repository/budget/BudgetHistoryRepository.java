package com.expense_tracker.repository.budget;

import com.expense_tracker.model.budget.BudgetHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetHistoryRepository extends JpaRepository<BudgetHistory,Long> {

    List<BudgetHistory> findByUserIdOrderByYearDescMonthDesc(Long userId);

    List<BudgetHistory> findByMonthAndYear(int month, int year);
}