package com.company.expense.service;

import com.company.expense.dto.ClaimReviewRequest;
import com.company.expense.dto.ExpenseClaimRequest;
import com.company.expense.dto.ExpenseClaimResponse;
import com.company.expense.entity.ClaimStatus;

import java.util.List;

public interface ExpenseClaimService {

    ExpenseClaimResponse submitClaim(ExpenseClaimRequest request);

    ExpenseClaimResponse approveClaim(Long claimId, ClaimReviewRequest request);

    ExpenseClaimResponse rejectClaim(Long claimId, ClaimReviewRequest request);

    ExpenseClaimResponse getClaimById(Long claimId);

    List<ExpenseClaimResponse> searchClaims(Long departmentId, ClaimStatus status,
                                             String category, Integer month, Integer year);
}
