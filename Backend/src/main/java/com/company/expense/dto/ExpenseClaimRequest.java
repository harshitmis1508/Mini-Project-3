package com.company.expense.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseClaimRequest {

    @NotBlank(message = "Employee name is required")
    @Size(min = 2, max = 100, message = "Employee name must be between 2 and 100 characters")
    @Pattern(regexp = "^[A-Za-z][A-Za-z .'-]*$", message = "Employee name may only contain letters, spaces, apostrophes, hyphens and periods")
    private String employeeName;

    @NotNull(message = "Department is required")
    private Long departmentId;

    @NotBlank(message = "Expense category is required")
    @Size(max = 100, message = "Expense category must not exceed 100 characters")
    private String category;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @DecimalMax(value = "99999999999.99", message = "Amount is unrealistically large")
    @Digits(integer = 13, fraction = 2, message = "Amount can have at most 2 decimal places")
    private BigDecimal amount;

    @NotNull(message = "Expense date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @PastOrPresent(message = "Expense date cannot be in the future")
    private LocalDate expenseDate;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDate getExpenseDate() { return expenseDate; }
    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
