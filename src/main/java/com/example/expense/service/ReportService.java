package com.example.expense.service;

import com.example.expense.model.Expense;
import com.example.expense.model.User;
import com.example.expense.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class ReportService {
    private final ExpenseRepository expenseRepository;

    public ReportService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Map<String, Object> monthlyReport(User user, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        List<Expense> list = expenseRepository.findByUserAndDateBetweenOrderByDateDesc(user, start, end);
        BigDecimal total = BigDecimal.ZERO;
        Map<String, BigDecimal> byCategory = new TreeMap<>();
        for (Expense e : list) {
            total = total.add(e.getAmount());
            byCategory.putIfAbsent(e.getCategory(), BigDecimal.ZERO);
            byCategory.put(e.getCategory(), byCategory.get(e.getCategory()).add(e.getAmount()));
        }
        Map<String,Object> result = new HashMap<>();
        result.put("total", total);
        result.put("byCategory", byCategory);
        result.put("items", list);
        return result;
    }
}
