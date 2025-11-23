package com.expense_tracker.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyTrendDTO {
    private String month; // Nov 2025
    private Double income;
    private Double expense;
    private Double savings;

}