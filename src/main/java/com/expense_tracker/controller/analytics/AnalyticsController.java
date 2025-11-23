package com.expense_tracker.controller.analytics;

import com.expense_tracker.dto.analytics.CategorySpendingDTO;
import com.expense_tracker.dto.analytics.MonthlySummaryDTO;
import com.expense_tracker.dto.analytics.MonthlyTrendDTO;
import com.expense_tracker.response.ApiResponse;
import com.expense_tracker.service.analytics.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/monthly-summary")
    public ApiResponse<MonthlySummaryDTO> getMonthlySummary(
            @RequestParam int month,
            @RequestParam int year
    ) {
        return new ApiResponse<>(
                "success",
                "Monthly summary retrieved",
                analyticsService.getMonthlySummary(month, year),
                200
        );
    }

    @GetMapping("/spending-by-category")
    public ApiResponse<List<CategorySpendingDTO>> getSpendingByCategory(
            @RequestParam int month,
            @RequestParam int year
    ) {
        return new ApiResponse<>(
                "success",
                "Spending by category retrieved",
                analyticsService.getSpendingByCategory(month, year),
                200
        );
    }

    @GetMapping("/trends")
    public ApiResponse<List<MonthlyTrendDTO>> getMonthlyTrends(
            @RequestParam(defaultValue = "6") int monthsBack
    ) {
        return new ApiResponse<>(
                "success",
                "Monthly trends retrieved",
                analyticsService.getMonthlyTrends(monthsBack),
                200
        );
    }

}