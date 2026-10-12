package com.example.expense.controller;

import com.example.expense.model.Income;
import com.example.expense.model.User;
import com.example.expense.repository.IncomeRepository;
import com.example.expense.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeRepository incomeRepository;
    private final UserService userService;

    public IncomeController(IncomeRepository incomeRepository, UserService userService) {
        this.incomeRepository = incomeRepository;
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("incomes", incomeRepository.findByUserOrderByDateDesc(user));
        return "incomes";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        Income i = new Income();
        i.setDate(LocalDate.now());
        model.addAttribute("income", i);
        return "income_form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Income income, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        income.setUser(user);
        if (income.getAmount()==null) income.setAmount(BigDecimal.ZERO);
        incomeRepository.save(income);
        return "redirect:/incomes";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, Principal principal) {
        Optional<Income> opt = incomeRepository.findById(id);
        if (opt.isEmpty()) return "redirect:/incomes";
        Income i = opt.get();
        if (!i.getUser().getUsername().equals(principal.getName())) return "redirect:/incomes";
        model.addAttribute("income", i);
        return "income_form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Principal principal) {
        Optional<Income> opt = incomeRepository.findById(id);
        if (opt.isPresent()) {
            Income i = opt.get();
            if (i.getUser().getUsername().equals(principal.getName())) {
                incomeRepository.delete(i);
            }
        }
        return "redirect:/incomes";
    }
}
