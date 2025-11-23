package com.expense_tracker.utility.mapper;

import com.expense_tracker.dto.budget.BudgetRequestDTO;
import com.expense_tracker.dto.budget.BudgetResponseDTO;
import com.expense_tracker.model.Category;
import com.expense_tracker.model.User;
import com.expense_tracker.model.budget.Budget;

public class BudgetMapper {

    public static Budget toEntity(BudgetRequestDTO dto, User user, Category category) {
        if (dto == null) return null;

        return Budget.builder()
                .amount(dto.getAmount())
                .month(dto.getMonth())
                .year(dto.getYear())
                .category(category)
                .user(user)
                .spent(0.0)
                .build();
    }


    public static BudgetResponseDTO toDTO(Budget budget) {
        if (budget == null) return null;

        Double remaining = budget.getAmount() - budget.getSpent();

        return new BudgetResponseDTO(
                budget.getId(),
                budget.getAmount(),
                budget.getMonth(),
                budget.getYear(),
                budget.getCategory() != null ? budget.getCategory().getId() : null,
                budget.getSpent(),
                remaining,
                budget.getUser().getId()
        );
    }
}