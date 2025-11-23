package com.expense_tracker.controller.budget;

import com.expense_tracker.dto.budget.BudgetRequestDTO;
import com.expense_tracker.dto.budget.BudgetResponseDTO;
import com.expense_tracker.response.ApiResponse;
import com.expense_tracker.service.budget.BudgetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<ApiResponse<BudgetResponseDTO>> createBudget(@RequestBody BudgetRequestDTO dto) {
        log.info("[BUDGET] [CREATE] Enter BudgetController::createBudget()");

        BudgetResponseDTO created = budgetService.createBudget(dto);
        ApiResponse<BudgetResponseDTO> response = new ApiResponse<>(
                "success",
                "Budget created",
                created,
                HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BudgetResponseDTO>>> getBudgets(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        log.info("[BUDGET] [CREATE] Enter BudgetController::getBudgets()");

        List<BudgetResponseDTO> budgets = budgetService.getBudgets(month, year);
        ApiResponse<List<BudgetResponseDTO>> response = new ApiResponse<>(
                "success",
                "Budgets retrieved",
                budgets,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BudgetResponseDTO>> updateBudget(
            @PathVariable Long id,
            @RequestBody BudgetRequestDTO dto
    ) {
        log.info("[BUDGET] [CREATE] Enter BudgetController::updateBudget()");

        BudgetResponseDTO updated = budgetService.updateBudget(id, dto);
        ApiResponse<BudgetResponseDTO> response = new ApiResponse<>(
                "success",
                "Budget updated",
                updated,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBudget(@PathVariable Long id) {
        log.info("[BUDGET] [CREATE] Enter BudgetController::deleteBudget()");

        budgetService.deleteBudget(id);
        ApiResponse<Void> response = new ApiResponse<>(
                "success",
                "Budget deleted",
                null,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    // Test endpoint to reset budgets
    @PostMapping("/reset")
    public ResponseEntity<ApiResponse<String>> resetBudgets(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("error", "User not authenticated", null, 401));
        }

        budgetService.resetBudgetsForUser(userDetails.getUsername());

        return ResponseEntity.ok(
                new ApiResponse<>("success", "Budget reset successfully", null, 200)
        );
    }

}