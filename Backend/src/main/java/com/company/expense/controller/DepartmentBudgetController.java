package com.company.expense.controller;

import com.company.expense.dto.DepartmentBudgetRequest;
import com.company.expense.dto.DepartmentBudgetResponse;
import com.company.expense.service.DepartmentBudgetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class DepartmentBudgetController {

    private final DepartmentBudgetService budgetService;

    public DepartmentBudgetController(DepartmentBudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<DepartmentBudgetResponse> createBudget(@Valid @RequestBody DepartmentBudgetRequest request) {
        DepartmentBudgetResponse created = budgetService.createBudget(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentBudgetResponse> updateBudget(@PathVariable Long id,
                                                                   @Valid @RequestBody DepartmentBudgetRequest request) {
        return ResponseEntity.ok(budgetService.updateBudget(id, request));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentBudgetResponse>> getAllBudgets(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        if (month != null && year != null) {
            return ResponseEntity.ok(budgetService.getBudgetsByMonthYear(month, year));
        }
        return ResponseEntity.ok(budgetService.getAllBudgets());
    }
}
