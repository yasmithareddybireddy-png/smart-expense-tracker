package com.example.expense.repository;

import com.example.expense.model.Income;
import com.example.expense.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByUserOrderByDateDesc(User user);
    List<Income> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate start, LocalDate end);
}
