package com.company.expense.dto;

import java.math.BigDecimal;

public class MonthlySummaryResponse {
    private Long departmentId;
    private String departmentName;
    private Integer month;
    private Integer year;
    private BigDecimal monthlyBudget;
    private BigDecimal totalApprovedExpense;
    private BigDecimal totalPendingExpense;
    private BigDecimal remainingBudget;
    private long pendingCount;
    private long approvedCount;
    private long rejectedCount;

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public BigDecimal getMonthlyBudget() { return monthlyBudget; }
    public void setMonthlyBudget(BigDecimal monthlyBudget) { this.monthlyBudget = monthlyBudget; }

    public BigDecimal getTotalApprovedExpense() { return totalApprovedExpense; }
    public void setTotalApprovedExpense(BigDecimal totalApprovedExpense) { this.totalApprovedExpense = totalApprovedExpense; }

    public BigDecimal getTotalPendingExpense() { return totalPendingExpense; }
    public void setTotalPendingExpense(BigDecimal totalPendingExpense) { this.totalPendingExpense = totalPendingExpense; }

    public BigDecimal getRemainingBudget() { return remainingBudget; }
    public void setRemainingBudget(BigDecimal remainingBudget) { this.remainingBudget = remainingBudget; }

    public long getPendingCount() { return pendingCount; }
    public void setPendingCount(long pendingCount) { this.pendingCount = pendingCount; }

    public long getApprovedCount() { return approvedCount; }
    public void setApprovedCount(long approvedCount) { this.approvedCount = approvedCount; }

    public long getRejectedCount() { return rejectedCount; }
    public void setRejectedCount(long rejectedCount) { this.rejectedCount = rejectedCount; }
}
