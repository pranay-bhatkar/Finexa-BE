package com.expense_tracker.model.budget;

import com.expense_tracker.model.Category;
import com.expense_tracker.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "budgets",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"month", "year", "category_id", "user_id"}
        )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private int month;

    private int year;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; // null = overall monthly budget

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    private Double spent = 0.0; // auto updated based on transactions
}