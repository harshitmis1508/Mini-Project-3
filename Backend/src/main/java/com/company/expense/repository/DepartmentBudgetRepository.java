package com.company.expense.repository;

import com.company.expense.entity.DepartmentBudget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentBudgetRepository extends JpaRepository<DepartmentBudget, Long> {

    Optional<DepartmentBudget> findByDepartmentIdAndBudgetMonthAndBudgetYear(
            Long departmentId, Integer month, Integer year);

    boolean existsByDepartmentIdAndBudgetMonthAndBudgetYear(
            Long departmentId, Integer month, Integer year);

    List<DepartmentBudget> findByBudgetMonthAndBudgetYear(Integer month, Integer year);
}
