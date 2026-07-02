package com.company.expense.controller;

import com.company.expense.dto.ClaimReviewRequest;
import com.company.expense.dto.ExpenseClaimRequest;
import com.company.expense.dto.ExpenseClaimResponse;
import com.company.expense.entity.ClaimStatus;
import com.company.expense.service.ExpenseClaimService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ExpenseClaimController {

    private final ExpenseClaimService claimService;

    public ExpenseClaimController(ExpenseClaimService claimService) {
        this.claimService = claimService;
    }

    // FR-1: Submit a new expense claim
    @PostMapping
    public ResponseEntity<ExpenseClaimResponse> submitClaim(@Valid @RequestBody ExpenseClaimRequest request) {
        ExpenseClaimResponse created = claimService.submitClaim(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // FR-2: Approve a pending claim
    @PutMapping("/{id}/approve")
    public ResponseEntity<ExpenseClaimResponse> approveClaim(@PathVariable Long id,
                                                               @jakarta.validation.Valid @RequestBody ClaimReviewRequest request) {
        return ResponseEntity.ok(claimService.approveClaim(id, request));
    }

    // FR-2: Reject a pending claim
    @PutMapping("/{id}/reject")
    public ResponseEntity<ExpenseClaimResponse> rejectClaim(@PathVariable Long id,
                                                              @jakarta.validation.Valid @RequestBody ClaimReviewRequest request) {
        return ResponseEntity.ok(claimService.rejectClaim(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseClaimResponse> getClaimById(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.getClaimById(id));
    }

    // FR-5: Expense Tracking - filter by department, status, category, month, year
    @GetMapping
    public ResponseEntity<List<ExpenseClaimResponse>> searchClaims(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) ClaimStatus status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        List<ExpenseClaimResponse> results = claimService.searchClaims(departmentId, status, category, month, year);
        return ResponseEntity.ok(results);
    }
}
