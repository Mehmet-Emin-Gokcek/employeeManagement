package com.api.employeeManagement.payroll.LeaveRequest;
import com.api.employeeManagement.payroll.Employee.Employee;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "EMPLOYEE_LEAVE_REQUEST")
public class LeaveRequest {

        private @Id @GeneratedValue Long id;

        @NotNull(message = "Description may not be null")
        @NotBlank(message = "Description may not be empty")
        private String description;

        @NotNull(message = "Status may not be null")
        private Status status;

        @ManyToOne
        @JoinColumn(name="employee_id", nullable=true)
        private Employee employee;

        LeaveRequest() {}

        LeaveRequest(String description, Status status) {

            this.description = description;
            this.status = status;
        }

        public Long getId() {
            return this.id;
        }

        public String getDescription() {
            return this.description;
        }

        public Status getStatus() {
            return this.status;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setStatus(Status status) {
            this.status = status;
        }


        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        public String toString() {
            return "Order{" +
                    "id=" + id +
                    ", description='" + description + '\'' +
                    ", status=" + status +
                    '}';
        }
    }
