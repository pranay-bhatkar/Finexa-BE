package com.expense_tracker.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategorySpendingDTO {
    private String categoryName;
    private Double amount;

}