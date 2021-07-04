package com.api.employeeManagement.payroll.LeaveRequest;

public class RequestNotFoundException extends RuntimeException {
    RequestNotFoundException(Long id) {
        super("Could not find order: " + id);
    }
}
