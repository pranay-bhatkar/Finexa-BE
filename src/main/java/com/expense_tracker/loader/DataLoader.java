package com.expense_tracker.loader;

import com.expense_tracker.model.Category;
import com.expense_tracker.model.CategoryType;
import com.expense_tracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category("Food", CategoryType.EXPENSE, "🍔", null));
            categoryRepository.save(new Category("Travel", CategoryType.EXPENSE, "✈️", null));
            categoryRepository.save(new Category("Rent", CategoryType.EXPENSE, "🏠", null));
            categoryRepository.save(new Category("Salary", CategoryType.INCOME, "💰", null));

        }
    }
}