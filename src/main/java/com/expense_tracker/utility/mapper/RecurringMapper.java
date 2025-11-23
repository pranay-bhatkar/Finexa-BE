package com.expense_tracker.utility.mapper;

import com.expense_tracker.dto.recurring.RecurringTransactionDTO;
import com.expense_tracker.model.recurring.RecurringTransaction;

public class RecurringMapper {

    public static RecurringTransactionDTO toDTO(RecurringTransaction recurring) {
        RecurringTransactionDTO.SimpleCategoryDTO categoryDTO = null;
        if (recurring.getCategory() != null) {
            categoryDTO = new RecurringTransactionDTO.SimpleCategoryDTO(
                    recurring.getCategory().getId(),
                    recurring.getCategory().getName(),
                    recurring.getCategory().getType() != null
                            ? recurring.getCategory().getType().name()
                            : null  // safely handle null
            );
        }

        return new RecurringTransactionDTO(
                recurring.getId(),
                recurring.getAmount(),
                recurring.getFrequency().name(),
                recurring.getNextExecutionDate().toString(),
                new RecurringTransactionDTO.SimpleUserDTO(
                        recurring.getUser().getId(),
                        recurring.getUser().getName(),
                        recurring.getUser().getEmail()
                ),
                categoryDTO
        );
    }
}