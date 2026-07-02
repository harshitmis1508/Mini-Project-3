package com.company.expense.dto;

import com.company.expense.entity.ClaimStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseClaimResponse {
    private Long id;
    private String employeeName;
    private Long departmentId;
    private String departmentName;
    private String category;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private String description;
    private ClaimStatus status;
    private String reviewRemark;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDate getExpenseDate() { return expenseDate; }
    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ClaimStatus getStatus() { return status; }
    public void setStatus(ClaimStatus status) { this.status = status; }

    public String getReviewRemark() { return reviewRemark; }
    public void setReviewRemark(String reviewRemark) { this.reviewRemark = reviewRemark; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
