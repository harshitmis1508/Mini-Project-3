package com.company.expense.repository;

import com.company.expense.entity.ClaimStatus;
import com.company.expense.entity.ExpenseClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseClaimRepository extends JpaRepository<ExpenseClaim, Long> {

    List<ExpenseClaim> findByDepartmentIdAndStatus(Long departmentId, ClaimStatus status);

    List<ExpenseClaim> findByStatus(ClaimStatus status);

    @Query("SELECT e FROM ExpenseClaim e WHERE " +
           "(:departmentId IS NULL OR e.department.id = :departmentId) AND " +
           "(:status IS NULL OR e.status = :status) AND " +
           "(:category IS NULL OR e.category = :category) AND " +
           "(:month IS NULL OR MONTH(e.expenseDate) = :month) AND " +
           "(:year IS NULL OR YEAR(e.expenseDate) = :year) " +
           "ORDER BY e.createdAt DESC")
    List<ExpenseClaim> searchClaims(
            @Param("departmentId") Long departmentId,
            @Param("status") ClaimStatus status,
            @Param("category") String category,
            @Param("month") Integer month,
            @Param("year") Integer year);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseClaim e " +
           "WHERE e.department.id = :departmentId AND e.status = :status " +
           "AND MONTH(e.expenseDate) = :month AND YEAR(e.expenseDate) = :year")
    BigDecimal sumAmountByDepartmentAndStatusAndMonthYear(
            @Param("departmentId") Long departmentId,
            @Param("status") ClaimStatus status,
            @Param("month") Integer month,
            @Param("year") Integer year);

    @Query("SELECT COUNT(e) FROM ExpenseClaim e WHERE e.department.id = :departmentId AND e.status = :status " +
           "AND MONTH(e.expenseDate) = :month AND YEAR(e.expenseDate) = :year")
    long countByDepartmentAndStatusAndMonthYear(
            @Param("departmentId") Long departmentId,
            @Param("status") ClaimStatus status,
            @Param("month") Integer month,
            @Param("year") Integer year);
}
