package com.example.expense.controller;

import com.example.expense.model.User;
import com.example.expense.service.ReportService;
import com.example.expense.service.UserService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.StringWriter;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;
    private final UserService userService;

    public ReportController(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    @GetMapping
    public String form(Model model) {
        LocalDate now = LocalDate.now();
        model.addAttribute("year", now.getYear());
        model.addAttribute("month", now.getMonthValue());
        return "report";
    }

    @PostMapping
    public String generate(@RequestParam int year, @RequestParam int month, Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Map<String, Object> r = reportService.monthlyReport(user, year, month);
        model.addAttribute("report", r);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        return "report";
    }

    @GetMapping("/csv")
    public ResponseEntity<byte[]> csv(@RequestParam int year, @RequestParam int month, Principal principal) throws Exception {
        User user = userService.findByUsername(principal.getName());
        Map<String, Object> r = reportService.monthlyReport(user, year, month);

        StringWriter out = new StringWriter();
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader("Date","Category","Description","Amount"))) {
            var items = (java.util.List<com.example.expense.model.Expense>) r.get("items");
            for (var e : items) {
                printer.printRecord(e.getDate(), e.getCategory(), e.getDescription(), e.getAmount());
            }
            printer.printRecord("", "TOTAL", "", r.get("total"));
        }
        byte[] bytes = out.toString().getBytes("UTF-8");
        String filename = String.format("report-%d-%02d.csv", year, month);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(bytes);
    }
}
