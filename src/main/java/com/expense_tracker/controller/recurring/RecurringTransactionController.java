package com.expense_tracker.controller.recurring;

import com.expense_tracker.dto.recurring.RecurringTransactionDTO;
import com.expense_tracker.dto.recurring.RecurringTransactionRequestDTO;
import com.expense_tracker.model.recurring.RecurringTransaction;
import com.expense_tracker.response.ApiResponse;
import com.expense_tracker.service.recurring.RecurringTransactionService;
import com.expense_tracker.utility.mapper.RecurringMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recurring")
@RequiredArgsConstructor
public class RecurringTransactionController {

    private final RecurringTransactionService recurringTransactionService;

    @PostMapping
    public ApiResponse<RecurringTransactionDTO> addRecurring(@RequestBody RecurringTransactionRequestDTO dto) {
        RecurringTransaction saved = recurringTransactionService.addRecurring(dto);

        // Map to DTO for safe response
        RecurringTransactionDTO responseDTO = RecurringMapper.toDTO(saved);

        return new ApiResponse<>(
                "success",
                "Recurring Transaction added successfully",
                responseDTO,
                HttpStatus.CREATED.value()
        );
    }


    @GetMapping
    public ApiResponse<List<RecurringTransactionDTO>> getAllRecurring() {
        List<RecurringTransactionDTO> dtoList = recurringTransactionService.getAllRecurring()
                .stream()
                .map(RecurringMapper::toDTO)
                .toList();

        return new ApiResponse<>(
                "success",
                "fetched all Recurring Transactions successfully",
                dtoList,
                HttpStatus.CREATED.value()
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRecurring(@PathVariable Long id) {
        recurringTransactionService.deleteRecurring(id);
        return new ApiResponse<>(
                "success",
                "Recurring transaction deleted successfully",
                null,
                HttpStatus.CREATED.value()
        );
    }

    // for the testing of the cron
    @PostMapping("/execute")
    public ApiResponse<Void> executeRecurringNow() {
        recurringTransactionService.executeRecurringTransactions();
        return new ApiResponse<>(
                "success",
                "Executed recurring transactions manually",
                null,
                HttpStatus.CREATED.value()
        );
    }


}