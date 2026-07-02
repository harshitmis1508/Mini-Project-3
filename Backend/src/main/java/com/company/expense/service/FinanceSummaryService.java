package com.company.expense.service;

import com.company.expense.dto.MonthlySummaryResponse;

import java.util.List;

public interface FinanceSummaryService {
    List<MonthlySummaryResponse> getMonthlySummary(Integer month, Integer year);
    MonthlySummaryResponse getDepartmentMonthlySummary(Long departmentId, Integer month, Integer year);
}
