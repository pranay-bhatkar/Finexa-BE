package com.expense_tracker.utility.mapper;

import com.expense_tracker.dto.transaction.TransactionResponseDTO;
import com.expense_tracker.model.Transaction;

public class TransactionMapper {

    public static TransactionResponseDTO toDTO(Transaction t) {
        TransactionResponseDTO dto = new TransactionResponseDTO();

        dto.setId(t.getId());
        dto.setAmount(t.getAmount());
        dto.setType(t.getType().name());
        dto.setNotes(t.getNotes());
        dto.setRecurring(t.isRecurring());
        dto.setDate(t.getDate());

        if (t.getCategory() != null) {
            dto.setCategoryId(t.getCategory().getId());
            dto.setCategoryName(t.getCategory().getName());
        } else {
            dto.setCategoryId(null);
            dto.setCategoryName(null);
        }

        return dto;
    }
}