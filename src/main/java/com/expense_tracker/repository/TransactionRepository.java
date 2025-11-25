package com.expense_tracker.repository;

import com.expense_tracker.model.Transaction;
import com.expense_tracker.model.TransactionType;
import com.expense_tracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

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
               AND FUNCTION('MONTH', t.date) = :month
               AND FUNCTION('YEAR', t.date) = :year
              AND t.archived = false
              AND t.type = 'EXPENSE'
              AND (:categoryId IS NULL OR t.category.id = :categoryId)
            """)
    double sumSpent(Long userId, Integer month, Integer year, Long categoryId);


    @Query("SELECT t.category.name, COALESCE(SUM(t.amount), 0) " +
            "FROM Transaction t " +
            "WHERE t.user.id = :userId " +
            "AND FUNCTION('MONTH', t.date) = :month " +
            "AND FUNCTION('YEAR', t.date) = :year " +
            "GROUP BY t.category.name")
    List<Object[]> getCategoryWiseSpending(@Param("userId") Long userId,
                                           @Param("month") int month,
                                           @Param("year") int year);


    // Monthly trend for last N months
    // TransactionRepository.java
    @Query(value = """
            SELECT YEAR(t.date) AS yr,
                   MONTH(t.date) AS mn,
                   SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END) AS income,
                   SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END) AS expense
            FROM transactions t
            WHERE t.user_id = :userId
              AND t.date >= :startDate
              AND t.archived = false
            GROUP BY YEAR(t.date), MONTH(t.date)
            ORDER BY YEAR(t.date), MONTH(t.date)
            """, nativeQuery = true)
    List<Object[]> getMonthlyTrendsNative(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate
    );


    @Query("SELECT COALESCE(SUM(t.amount), 0) " +
            "FROM Transaction t " +
            "WHERE t.user.id = :userId " +
            "AND FUNCTION('MONTH', t.date) = :month " +
            "AND FUNCTION('YEAR', t.date) = :year " +
            "AND t.type = :type")
    Double sumByUserAndMonthAndType(@Param("userId") Long userId,
                                    @Param("month") int month,
                                    @Param("year") int year,
                                    @Param("type") TransactionType type);


    @Query("SELECT t FROM Transaction t " +
            "WHERE t.user.id = :userId " +
            "AND FUNCTION('MONTH', t.date) = :month " +
            "AND FUNCTION('YEAR', t.date) = :year ")
    List<Transaction> findByUserIdAndMonthAndYear(
            @Param("userId") Long userId,
            @Param("month") int month,
            @Param("year") int year
    );


    @Query("SELECT t FROM Transaction t WHERE t.recurring = true AND t.date = :date")
    List<Transaction> findByRecurringDate(@Param("date") LocalDate date);

    @Query("SELECT DISTINCT t.user FROM Transaction t")
    List<User> findDistinctUsers();


    // Sum of income for a user in a date range
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.type = 'INCOME' " +
            "AND t.date BETWEEN :start AND :end")
    double sumIncome(@Param("userId") Long userId,
                     @Param("start") LocalDate start,
                     @Param("end") LocalDate end);


    // Sum of expense for a user in a date range
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.type = 'EXPENSE' " +
            "AND t.date BETWEEN :start AND :end")
    double sumExpense(@Param("userId") Long userId,
                      @Param("start") LocalDate start,
                      @Param("end") LocalDate end);


    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type='INCOME'")
    double sumAllIncome();

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type='EXPENSE'")
    double sumAllExpenses();

    @Query("SELECT COUNT(r) FROM RecurringTransaction r")
    long countRecurringTransactions();


}