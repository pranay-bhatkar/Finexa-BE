package com.expense_tracker.repository.recurring;

import com.expense_tracker.model.recurring.RecurringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, Long> {

    List<RecurringTransaction> findByNextExecutionDateLessThanEqual(LocalDate date);
}