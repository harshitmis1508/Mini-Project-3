package com.company.expense.service;

import com.company.expense.dto.DepartmentDto;
import com.company.expense.dto.DepartmentRequest;

import java.util.List;

public interface DepartmentService {
    DepartmentDto createDepartment(DepartmentRequest request);
    List<DepartmentDto> getAllDepartments();
    DepartmentDto getDepartmentById(Long id);
}
