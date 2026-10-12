package com.example.expense.controller;

import com.example.expense.model.Budget;
import com.example.expense.model.User;
import com.example.expense.repository.BudgetRepository;
import com.example.expense.repository.ExpenseRepository;
import com.example.expense.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/budgets")
public class BudgetController {
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    public BudgetController(BudgetRepository budgetRepository, ExpenseRepository expenseRepository, UserService userService) {
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        LocalDate now = LocalDate.now();
        int y = now.getYear(), m = now.getMonthValue();
        var opt = budgetRepository.findByUserAndYearAndMonth(user,y,m);
        Budget b = opt.orElseGet(() -> {
            Budget nb = new Budget();
            nb.setYear(y); nb.setMonth(m); nb.setAmount(BigDecimal.ZERO); nb.setUser(user);
            return nb;
        });
        var start = LocalDate.of(y, m, 1);
        var end = start.withDayOfMonth(start.lengthOfMonth());
        var spent = expenseRepository.findByUserAndDateBetweenOrderByDateDesc(user, start, end)
                .stream().map(e -> e.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("budget", b);
        model.addAttribute("spent", spent);
        return "budgets";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Budget budget, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        budget.setUser(user);
        budgetRepository.findByUserAndYearAndMonth(user, budget.getYear(), budget.getMonth())
                .ifPresentOrElse(existing -> {
                    existing.setAmount(budget.getAmount());
                    existing.setAlertPercent(budget.getAlertPercent());
                    budgetRepository.save(existing);
                }, () -> budgetRepository.save(budget));
        return "redirect:/budgets";
    }
}
