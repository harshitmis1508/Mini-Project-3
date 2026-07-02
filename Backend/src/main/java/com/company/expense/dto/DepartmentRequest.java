package com.company.expense.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DepartmentRequest {

    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9 &.'-]*$", message = "Department name contains invalid characters")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
