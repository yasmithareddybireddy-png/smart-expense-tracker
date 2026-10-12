package com.example.expense.controller;

import com.example.expense.model.Expense;
import com.example.expense.model.User;
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
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    public ExpenseController(ExpenseRepository expenseRepository, UserService userService) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("expenses", expenseRepository.findByUserOrderByDateDesc(user));
        return "expenses";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        Expense e = new Expense();
        e.setDate(LocalDate.now());
        model.addAttribute("expense", e);
        return "expense_form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Expense expense, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        expense.setUser(user);
        if (expense.getAmount()==null) expense.setAmount(BigDecimal.ZERO);
        expenseRepository.save(expense);
        return "redirect:/expenses";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, Principal principal) {
        Optional<Expense> opt = expenseRepository.findById(id);
        if (opt.isEmpty()) return "redirect:/expenses";
        Expense e = opt.get();
        if (!e.getUser().getUsername().equals(principal.getName())) return "redirect:/expenses";
        model.addAttribute("expense", e);
        return "expense_form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Principal principal) {
        Optional<Expense> opt = expenseRepository.findById(id);
        if (opt.isPresent()) {
            Expense e = opt.get();
            if (e.getUser().getUsername().equals(principal.getName())) {
                expenseRepository.delete(e);
            }
        }
        return "redirect:/expenses";
    }
}
