package com.company.expense.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<DepartmentBudget> budgets = new ArrayList<>();

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<ExpenseClaim> claims = new ArrayList<>();

    public Department() {}

    public Department(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<DepartmentBudget> getBudgets() { return budgets; }
    public void setBudgets(List<DepartmentBudget> budgets) { this.budgets = budgets; }

    public List<ExpenseClaim> getClaims() { return claims; }
    public void setClaims(List<ExpenseClaim> claims) { this.claims = claims; }
}
