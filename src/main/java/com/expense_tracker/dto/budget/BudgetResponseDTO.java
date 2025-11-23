package com.expense_tracker.dto.budget;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BudgetResponseDTO {
    private Long id;
    private Double amount;
    private Integer month;
    private Integer year;
    private Long categoryId;
    private Double spent;
    private Double remaining;
    private Long userId;
}