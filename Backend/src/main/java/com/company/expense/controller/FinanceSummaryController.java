package com.company.expense.controller;

import com.company.expense.dto.MonthlySummaryResponse;
import com.company.expense.service.FinanceSummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/summary")
public class FinanceSummaryController {

    private final FinanceSummaryService summaryService;

    public FinanceSummaryController(FinanceSummaryService summaryService) {
        this.summaryService = summaryService;
    }

    // FR-6: Monthly finance summary for all departments
    @GetMapping
    public ResponseEntity<List<MonthlySummaryResponse>> getMonthlySummary(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return ResponseEntity.ok(summaryService.getMonthlySummary(month, year));
    }

    // FR-6: Monthly finance summary for one department
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<MonthlySummaryResponse> getDepartmentMonthlySummary(
            @PathVariable Long departmentId,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return ResponseEntity.ok(summaryService.getDepartmentMonthlySummary(departmentId, month, year));
    }
}
