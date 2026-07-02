package com.company.expense.dto;

import jakarta.validation.constraints.Size;

public class ClaimReviewRequest {

    @Size(max = 500, message = "Review remark must not exceed 500 characters")
    private String reviewRemark;

    public String getReviewRemark() { return reviewRemark; }
    public void setReviewRemark(String reviewRemark) { this.reviewRemark = reviewRemark; }
}
