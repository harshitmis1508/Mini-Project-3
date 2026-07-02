package com.company.expense.service.impl;

import com.company.expense.dto.ClaimReviewRequest;
import com.company.expense.dto.ExpenseClaimRequest;
import com.company.expense.dto.ExpenseClaimResponse;
import com.company.expense.entity.ClaimStatus;
import com.company.expense.entity.Department;
import com.company.expense.entity.DepartmentBudget;
import com.company.expense.entity.ExpenseClaim;
import com.company.expense.exception.BusinessRuleException;
import com.company.expense.exception.ResourceNotFoundException;
import com.company.expense.repository.DepartmentBudgetRepository;
import com.company.expense.repository.DepartmentRepository;
import com.company.expense.repository.ExpenseClaimRepository;
import com.company.expense.service.ExpenseClaimService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseClaimServiceImpl implements ExpenseClaimService {

    private final ExpenseClaimRepository claimRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentBudgetRepository budgetRepository;

    public ExpenseClaimServiceImpl(ExpenseClaimRepository claimRepository,
                                    DepartmentRepository departmentRepository,
                                    DepartmentBudgetRepository budgetRepository) {
        this.claimRepository = claimRepository;
        this.departmentRepository = departmentRepository;
        this.budgetRepository = budgetRepository;
    }

    @Override
    public ExpenseClaimResponse submitClaim(ExpenseClaimRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id: " + request.getDepartmentId()));

        // Real-world sanity check: a single claim cannot exceed the department's entire
        // monthly budget for the month/year it was incurred in (if a budget has been set).
        int expMonth = request.getExpenseDate().getMonthValue();
        int expYear = request.getExpenseDate().getYear();
        budgetRepository.findByDepartmentIdAndBudgetMonthAndBudgetYear(
                        request.getDepartmentId(), expMonth, expYear)
                .ifPresent(budget -> {
                    if (request.getAmount().compareTo(budget.getBudgetAmount()) > 0) {
                        throw new BusinessRuleException(
                                "Claim amount (" + request.getAmount() + ") exceeds the department's " +
                                        "entire monthly budget (" + budget.getBudgetAmount() + ") for " +
                                        expMonth + "/" + expYear);
                    }
                });

        ExpenseClaim claim = new ExpenseClaim();
        claim.setEmployeeName(request.getEmployeeName().trim());
        claim.setDepartment(department);
        claim.setCategory(request.getCategory().trim());
        claim.setAmount(request.getAmount());
        claim.setExpenseDate(request.getExpenseDate());
        claim.setDescription(request.getDescription() == null ? null : request.getDescription().trim());
        claim.setStatus(ClaimStatus.PENDING); // FR-1: new claims are always Pending

        ExpenseClaim saved = claimRepository.save(claim);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public ExpenseClaimResponse approveClaim(Long claimId, ClaimReviewRequest request) {
        ExpenseClaim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + claimId));

        // Validation: only pending claims can be approved
        if (claim.getStatus() != ClaimStatus.PENDING) {
            throw new BusinessRuleException("Only pending claims can be approved");
        }

        int month = claim.getExpenseDate().getMonthValue();
        int year = claim.getExpenseDate().getYear();
        Long departmentId = claim.getDepartment().getId();

        DepartmentBudget budget = budgetRepository
                .findByDepartmentIdAndBudgetMonthAndBudgetYear(departmentId, month, year)
                .orElseThrow(() -> new BusinessRuleException(
                        "No budget defined for this department for " + month + "/" + year));

        BigDecimal alreadyApproved = claimRepository.sumAmountByDepartmentAndStatusAndMonthYear(
                departmentId, ClaimStatus.APPROVED, month, year);

        BigDecimal projectedTotal = alreadyApproved.add(claim.getAmount());

        // FR-4: Budget Control - cannot approve if it exceeds monthly budget
        if (projectedTotal.compareTo(budget.getBudgetAmount()) > 0) {
            throw new BusinessRuleException(
                    "Approval denied: this would exceed the department's monthly budget. " +
                    "Budget: " + budget.getBudgetAmount() + ", Already approved: " + alreadyApproved +
                    ", Claim amount: " + claim.getAmount());
        }

        claim.setStatus(ClaimStatus.APPROVED);
        claim.setReviewRemark(request.getReviewRemark());
        ExpenseClaim saved = claimRepository.save(claim);
        return toResponse(saved);
    }

    @Override
    public ExpenseClaimResponse rejectClaim(Long claimId, ClaimReviewRequest request) {
        ExpenseClaim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + claimId));

        if (claim.getStatus() != ClaimStatus.PENDING) {
            throw new BusinessRuleException("Only pending claims can be rejected");
        }

        // Real-world rule: a rejection must always be accompanied by a reason
        if (request.getReviewRemark() == null || request.getReviewRemark().trim().isEmpty()) {
            throw new BusinessRuleException("A review remark is required when rejecting a claim");
        }

        // FR-4: Rejected claims do not affect budget usage - no budget calculation needed here
        claim.setStatus(ClaimStatus.REJECTED);
        claim.setReviewRemark(request.getReviewRemark().trim());
        ExpenseClaim saved = claimRepository.save(claim);
        return toResponse(saved);
    }

    @Override
    public ExpenseClaimResponse getClaimById(Long claimId) {
        ExpenseClaim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + claimId));
        return toResponse(claim);
    }

    @Override
    public List<ExpenseClaimResponse> searchClaims(Long departmentId, ClaimStatus status,
                                                     String category, Integer month, Integer year) {
        return claimRepository.searchClaims(departmentId, status, category, month, year)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ExpenseClaimResponse toResponse(ExpenseClaim claim) {
        ExpenseClaimResponse response = new ExpenseClaimResponse();
        response.setId(claim.getId());
        response.setEmployeeName(claim.getEmployeeName());
        response.setDepartmentId(claim.getDepartment().getId());
        response.setDepartmentName(claim.getDepartment().getName());
        response.setCategory(claim.getCategory());
        response.setAmount(claim.getAmount());
        response.setExpenseDate(claim.getExpenseDate());
        response.setDescription(claim.getDescription());
        response.setStatus(claim.getStatus());
        response.setReviewRemark(claim.getReviewRemark());
        response.setCreatedAt(claim.getCreatedAt());
        return response;
    }
}
