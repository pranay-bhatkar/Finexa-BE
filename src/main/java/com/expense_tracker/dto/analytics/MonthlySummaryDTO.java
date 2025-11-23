package com.expense_tracker.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlySummaryDTO {
    private Double totalIncome;
    private Double totalExpense;
    private Double savings; // income - expense

}