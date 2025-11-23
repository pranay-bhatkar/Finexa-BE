package com.expense_tracker.dto.transaction;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionResponseDTO {

    private Long id;
    private Long categoryId;
    private String categoryName;
    private String type;
    private Double amount;
    private String notes;
    private Boolean recurring;
    private LocalDate date;
}