package com.company.expense.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class DepartmentBudgetRequest {

    @NotNull(message = "Department is required")
    private Long departmentId;

    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer budgetMonth;

    @NotNull(message = "Year is required")
    @Min(value = 2000, message = "Year must be 2000 or later")
    @Max(value = 2100, message = "Year must be 2100 or earlier")
    private Integer budgetYear;

    @NotNull(message = "Budget amount is required")
    @DecimalMin(value = "0.01", message = "Budget amount must be greater than zero")
    @DecimalMax(value = "99999999999.99", message = "Budget amount is unrealistically large")
    @Digits(integer = 13, fraction = 2, message = "Budget amount can have at most 2 decimal places")
    private BigDecimal budgetAmount;

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public Integer getBudgetMonth() { return budgetMonth; }
    public void setBudgetMonth(Integer budgetMonth) { this.budgetMonth = budgetMonth; }

    public Integer getBudgetYear() { return budgetYear; }
    public void setBudgetYear(Integer budgetYear) { this.budgetYear = budgetYear; }

    public BigDecimal getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }
}
