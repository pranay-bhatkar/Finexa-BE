package com.expense_tracker.service.analytics;

import com.expense_tracker.dto.analytics.CategorySpendingDTO;
import com.expense_tracker.dto.analytics.MonthlySummaryDTO;
import com.expense_tracker.dto.analytics.MonthlyTrendDTO;
import com.expense_tracker.model.TransactionType;
import com.expense_tracker.model.User;
import com.expense_tracker.repository.TransactionRepository;
import com.expense_tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    // monthly summary
    public MonthlySummaryDTO getMonthlySummary(int month, int year) {
        User user = userService.getCurrentUser();

        Double income = transactionRepository.sumByUserAndMonthAndType(user.getId(), month, year, TransactionType.INCOME);
        Double expense = transactionRepository.sumByUserAndMonthAndType(user.getId(), month, year, TransactionType.EXPENSE);

        income = income == null ? 0.0 : income;
        expense = expense == null ? 0.0 : expense;

        Double savings = income - expense;

        return new MonthlySummaryDTO(income, expense, savings);
    }

    // spending by category (for pie chart)
    public List<CategorySpendingDTO> getSpendingByCategory(int month, int year) {
        User user = userService.getCurrentUser();

        return transactionRepository.getCategoryWiseSpending(user.getId(), month, year)
                .stream()
                .map(obj -> new CategorySpendingDTO((String) obj[0], (Double) obj[1]))
                .collect(Collectors.toList());
    }


    public List<MonthlyTrendDTO> getMonthlyTrends(int monthsBack) {
        User user = userService.getCurrentUser();

        // Start date: first day of N months back
        LocalDate startDate = LocalDate.now().minusMonths(monthsBack).withDayOfMonth(1);

        return transactionRepository.getMonthlyTrendsNative(user.getId(), startDate)
                .stream()
                .map(obj -> {
                    int year = ((Number) obj[0]).intValue();
                    int month = ((Number) obj[1]).intValue();

                    String monthName = YearMonth.of(year, month)
                            .getMonth()
                            .getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + year;

                    double income = ((Number) obj[2]).doubleValue();
                    double expense = ((Number) obj[3]).doubleValue();

                    return new MonthlyTrendDTO(monthName, income, expense, income - expense);
                })
                .collect(Collectors.toList());
    }


}