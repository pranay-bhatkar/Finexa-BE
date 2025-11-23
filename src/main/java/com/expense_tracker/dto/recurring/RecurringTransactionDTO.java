package com.expense_tracker.dto.recurring;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecurringTransactionDTO {
    private Long id;
    private Double amount;
    private String frequency;
    private String nextExecutionDate;
    private SimpleUserDTO user;
    private SimpleCategoryDTO category;

    @Data
    @AllArgsConstructor
    public static class SimpleUserDTO {
        private Long id;
        private String name;
        private String email;
    }

    @Data
    @AllArgsConstructor
    public static class SimpleCategoryDTO {
        private Long id;
        private String name;
        private String type;
    }
}