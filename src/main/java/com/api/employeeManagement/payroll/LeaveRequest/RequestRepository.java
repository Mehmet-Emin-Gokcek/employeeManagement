package com.api.employeeManagement.payroll.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository  extends JpaRepository<LeaveRequest, Long> {
}
