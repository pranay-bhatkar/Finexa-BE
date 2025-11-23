package com.expense_tracker.dto.recurring;


import com.expense_tracker.model.recurring.Frequency;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RecurringTransactionRequestDTO {

    @NotNull
    private Double amount;

    @NotNull
    private Frequency frequency; // WEEKLY, MONTHLY, YEARLY

    @NotNull
    private LocalDate nextExecutionDate;

    @NotNull
    private Long userId;

    private Long categoryId; // optional
}