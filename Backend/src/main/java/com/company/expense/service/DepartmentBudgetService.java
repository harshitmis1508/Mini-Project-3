package com.company.expense.service;

import com.company.expense.dto.DepartmentBudgetRequest;
import com.company.expense.dto.DepartmentBudgetResponse;

import java.util.List;

public interface DepartmentBudgetService {
    DepartmentBudgetResponse createBudget(DepartmentBudgetRequest request);
    DepartmentBudgetResponse updateBudget(Long id, DepartmentBudgetRequest request);
    List<DepartmentBudgetResponse> getAllBudgets();
    List<DepartmentBudgetResponse> getBudgetsByMonthYear(Integer month, Integer year);
}
