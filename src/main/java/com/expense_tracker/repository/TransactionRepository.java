package com.expense_tracker.repository;

import com.expense_tracker.model.Transaction;
import com.expense_tracker.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByUser_IdAndArchivedFalse(Long userId, Pageable pageable);

    Page<Transaction> findByUser_IdAndTypeAndArchivedFalse(
            Long userId,
            TransactionType type,
            Pageable pageable);

    Page<Transaction> findByUser_IdAndCategory_IdAndArchivedFalse(
            Long userId,
            Long categoryId,
            Pageable pageable);

    Page<Transaction> findByUser_IdAndDateBetweenAndArchivedFalse(
            Long userId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable);

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.user.id = :userId
              AND MONTH(t.date) = :month
              AND YEAR(t.date) = :year
              AND t.archived = false
              AND t.type = 'EXPENSE'
              AND (:categoryId IS NULL OR t.category.id = :categoryId)
            """)
    double sumSpent(Long userId, Integer month, Integer year, Long categoryId);


}