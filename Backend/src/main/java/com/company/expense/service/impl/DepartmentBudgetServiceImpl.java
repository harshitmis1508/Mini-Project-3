package com.company.expense.service.impl;

import com.company.expense.dto.DepartmentBudgetRequest;
import com.company.expense.dto.DepartmentBudgetResponse;
import com.company.expense.entity.ClaimStatus;
import com.company.expense.entity.Department;
import com.company.expense.entity.DepartmentBudget;
import com.company.expense.exception.BusinessRuleException;
import com.company.expense.exception.ResourceNotFoundException;
import com.company.expense.repository.DepartmentBudgetRepository;
import com.company.expense.repository.DepartmentRepository;
import com.company.expense.repository.ExpenseClaimRepository;
import com.company.expense.service.DepartmentBudgetService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentBudgetServiceImpl implements DepartmentBudgetService {

    private final DepartmentBudgetRepository budgetRepository;
    private final DepartmentRepository departmentRepository;
    private final ExpenseClaimRepository claimRepository;

    public DepartmentBudgetServiceImpl(DepartmentBudgetRepository budgetRepository,
                                        DepartmentRepository departmentRepository,
                                        ExpenseClaimRepository claimRepository) {
        this.budgetRepository = budgetRepository;
        this.departmentRepository = departmentRepository;
        this.claimRepository = claimRepository;
    }

    @Override
    public DepartmentBudgetResponse createBudget(DepartmentBudgetRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id: " + request.getDepartmentId()));

        // FR-3 / Validation: only one budget per department per month/year
        boolean exists = budgetRepository.existsByDepartmentIdAndBudgetMonthAndBudgetYear(
                request.getDepartmentId(), request.getBudgetMonth(), request.getBudgetYear());
        if (exists) {
            throw new BusinessRuleException(
                    "Budget already exists for this department for the given month and year");
        }

        validateBudgetCoversApprovedClaims(request.getDepartmentId(), request.getBudgetMonth(),
                request.getBudgetYear(), request.getBudgetAmount());

        DepartmentBudget budget = new DepartmentBudget();
        budget.setDepartment(department);
        budget.setBudgetMonth(request.getBudgetMonth());
        budget.setBudgetYear(request.getBudgetYear());
        budget.setBudgetAmount(request.getBudgetAmount());

        DepartmentBudget saved = budgetRepository.save(budget);
        return toResponse(saved);
    }

    @Override
    public DepartmentBudgetResponse updateBudget(Long id, DepartmentBudgetRequest request) {
        DepartmentBudget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + id));

        validateBudgetCoversApprovedClaims(budget.getDepartment().getId(), budget.getBudgetMonth(),
                budget.getBudgetYear(), request.getBudgetAmount());

        budget.setBudgetAmount(request.getBudgetAmount());
        DepartmentBudget saved = budgetRepository.save(budget);
        return toResponse(saved);
    }

    @Override
    public List<DepartmentBudgetResponse> getAllBudgets() {
        return budgetRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentBudgetResponse> getBudgetsByMonthYear(Integer month, Integer year) {
        return budgetRepository.findByBudgetMonthAndBudgetYear(month, year)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Real-world rule: a department's monthly budget can never be set below the amount
     * already approved/committed for that month, since approved claims have already been
     * promised against that budget. This is the inverse safeguard of FR-4 (claim approval
     * cannot exceed the budget) applied at budget creation/edit time.
     */
    private void validateBudgetCoversApprovedClaims(Long departmentId, Integer month, Integer year,
                                                      BigDecimal newBudgetAmount) {
        BigDecimal alreadyApproved = claimRepository.sumAmountByDepartmentAndStatusAndMonthYear(
                departmentId, ClaimStatus.APPROVED, month, year);
        if (alreadyApproved != null && newBudgetAmount.compareTo(alreadyApproved) < 0) {
            throw new BusinessRuleException(
                    "Budget amount cannot be less than the total already-approved claims (" +
                            alreadyApproved + ") for this department and period");
        }
    }

    private DepartmentBudgetResponse toResponse(DepartmentBudget budget) {
        DepartmentBudgetResponse response = new DepartmentBudgetResponse();
        response.setId(budget.getId());
        response.setDepartmentId(budget.getDepartment().getId());
        response.setDepartmentName(budget.getDepartment().getName());
        response.setBudgetMonth(budget.getBudgetMonth());
        response.setBudgetYear(budget.getBudgetYear());
        response.setBudgetAmount(budget.getBudgetAmount());
        return response;
    }
}
