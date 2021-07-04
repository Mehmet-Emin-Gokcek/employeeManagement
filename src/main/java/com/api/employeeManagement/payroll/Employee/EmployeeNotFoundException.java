package com.api.employeeManagement.payroll.Employee;

public class EmployeeNotFoundException extends RuntimeException {
    EmployeeNotFoundException(Long id) {
        super("Could not find employee " + id);
    }
}