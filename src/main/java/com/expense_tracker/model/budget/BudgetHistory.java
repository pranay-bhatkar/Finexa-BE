package com.expense_tracker.model.budget;

import com.expense_tracker.model.Category;
import com.expense_tracker.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "budget_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long originalBudgetId; // reference to original budget

    private Double amount;

    private Double spent;

    private Integer month;

    private Integer year;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime archivedAt = LocalDateTime.now();

}