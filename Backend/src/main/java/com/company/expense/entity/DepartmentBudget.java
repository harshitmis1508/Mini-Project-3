package com.company.expense.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "department_budget",
        uniqueConstraints = @UniqueConstraint(columnNames = {"department_id", "budget_month", "budget_year"}))
public class DepartmentBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "budget_month", nullable = false)
    private Integer budgetMonth;

    @Column(name = "budget_year", nullable = false)
    private Integer budgetYear;

    @Column(name = "budget_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal budgetAmount;

    public DepartmentBudget() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public Integer getBudgetMonth() { return budgetMonth; }
    public void setBudgetMonth(Integer budgetMonth) { this.budgetMonth = budgetMonth; }

    public Integer getBudgetYear() { return budgetYear; }
    public void setBudgetYear(Integer budgetYear) { this.budgetYear = budgetYear; }

    public BigDecimal getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }
}
