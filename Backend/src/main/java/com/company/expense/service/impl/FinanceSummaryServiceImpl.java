package com.company.expense.service.impl;

import com.company.expense.dto.MonthlySummaryResponse;
import com.company.expense.entity.ClaimStatus;
import com.company.expense.entity.Department;
import com.company.expense.entity.DepartmentBudget;
import com.company.expense.exception.ResourceNotFoundException;
import com.company.expense.repository.DepartmentBudgetRepository;
import com.company.expense.repository.DepartmentRepository;
import com.company.expense.repository.ExpenseClaimRepository;
import com.company.expense.service.FinanceSummaryService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FinanceSummaryServiceImpl implements FinanceSummaryService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentBudgetRepository budgetRepository;
    private final ExpenseClaimRepository claimRepository;

    public FinanceSummaryServiceImpl(DepartmentRepository departmentRepository,
                                      DepartmentBudgetRepository budgetRepository,
                                      ExpenseClaimRepository claimRepository) {
        this.departmentRepository = departmentRepository;
        this.budgetRepository = budgetRepository;
        this.claimRepository = claimRepository;
    }

    @Override
    public List<MonthlySummaryResponse> getMonthlySummary(Integer month, Integer year) {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(dept -> buildSummary(dept, month, year))
                .collect(Collectors.toList());
    }

    @Override
    public MonthlySummaryResponse getDepartmentMonthlySummary(Long departmentId, Integer month, Integer year) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));
        return buildSummary(department, month, year);
    }

    private MonthlySummaryResponse buildSummary(Department department, Integer month, Integer year) {
        Long departmentId = department.getId();

        Optional<DepartmentBudget> budgetOpt = budgetRepository
                .findByDepartmentIdAndBudgetMonthAndBudgetYear(departmentId, month, year);

        BigDecimal monthlyBudget = budgetOpt.map(DepartmentBudget::getBudgetAmount).orElse(BigDecimal.ZERO);

        BigDecimal approvedTotal = claimRepository.sumAmountByDepartmentAndStatusAndMonthYear(
                departmentId, ClaimStatus.APPROVED, month, year);
        BigDecimal pendingTotal = claimRepository.sumAmountByDepartmentAndStatusAndMonthYear(
                departmentId, ClaimStatus.PENDING, month, year);

        long pendingCount = claimRepository.countByDepartmentAndStatusAndMonthYear(
                departmentId, ClaimStatus.PENDING, month, year);
        long approvedCount = claimRepository.countByDepartmentAndStatusAndMonthYear(
                departmentId, ClaimStatus.APPROVED, month, year);
        long rejectedCount = claimRepository.countByDepartmentAndStatusAndMonthYear(
                departmentId, ClaimStatus.REJECTED, month, year);

        MonthlySummaryResponse summary = new MonthlySummaryResponse();
        summary.setDepartmentId(departmentId);
        summary.setDepartmentName(department.getName());
        summary.setMonth(month);
        summary.setYear(year);
        summary.setMonthlyBudget(monthlyBudget);
        summary.setTotalApprovedExpense(approvedTotal);
        summary.setTotalPendingExpense(pendingTotal);
        summary.setRemainingBudget(monthlyBudget.subtract(approvedTotal));
        summary.setPendingCount(pendingCount);
        summary.setApprovedCount(approvedCount);
        summary.setRejectedCount(rejectedCount);

        return summary;
    }
}
