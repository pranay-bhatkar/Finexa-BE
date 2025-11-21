package com.expense_tracker.dto.budget;

import lombok.Data;

@Data
public class BudgetRequestDTO {
    private Double amount;
    private Integer month;
    private Integer year;
    private Long categoryId; // null -> overall
}