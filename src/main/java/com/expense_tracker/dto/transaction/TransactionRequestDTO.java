package com.expense_tracker.dto.transaction;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionRequestDTO {

    private Long categoryId;      // category to link
    private String type;          // INCOME / EXPENSE
    private Double amount;
    private String notes;
    private Boolean recurring;    // true/false
    private LocalDate date;       // transaction date
}