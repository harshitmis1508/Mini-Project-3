package com.company.expense.service.impl;

import com.company.expense.dto.DepartmentDto;
import com.company.expense.dto.DepartmentRequest;
import com.company.expense.entity.Department;
import com.company.expense.exception.BusinessRuleException;
import com.company.expense.exception.ResourceNotFoundException;
import com.company.expense.repository.DepartmentRepository;
import com.company.expense.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public DepartmentDto createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BusinessRuleException("Department with this name already exists");
        }
        Department department = new Department(request.getName());
        Department saved = departmentRepository.save(department);
        return toDto(saved);
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return toDto(department);
    }

    private DepartmentDto toDto(Department department) {
        return new DepartmentDto(department.getId(), department.getName());
    }
}
