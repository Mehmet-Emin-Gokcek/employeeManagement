package com.api.employeeManagement.payroll.Employee;
import com.api.employeeManagement.payroll.LeaveRequest.LeaveRequest;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="EMPLOYEE")
public class Employee {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Role may not be null")
    @NotBlank(message = "Role may not be empty")
    private String role;

    @NotNull(message = "First Name may not be null")
    @NotBlank(message = "First Name may not be empty")
    private String firstName;

    @NotNull(message = "Last Name may not be null")
    @NotBlank(message = "Last Name may not be empty")
    private String lastName;

    @NotNull(message = "Email may not be null")
    @NotBlank(message = "Email may not be empty")
    private String email;

    @NotNull(message = "Date of Birth may not be null")
    @NotBlank(message = "Date of Birth may not be empty")
    private String dob;

    @NotNull(message = "Address may not be null")
    @NotBlank(message = "Address may not be empty")
    private String address;

    @NotNull(message = "Phone number may not be null")
    @NotBlank(message = "Phone may not be empty")
    private String phone;

    //By including the mappedBy attribute in the Employee class,
    //we mark it as the inverse side, meaning LeaveRequest class will be the owning side.
    @OneToMany(mappedBy="employee")
    private Set<LeaveRequest> requests;

    public Employee() {
    }

    public Employee(Long id, String firstName, String lastName, String role, String email, String dob, String address, String phone) {
        this.id = id;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
        this.address = address;
        this.phone = phone;
    }

    public Employee(String role, String firstName, String lastName, String email, String dob, String address, String phone) {
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
        this.address = address;
        this.phone = phone;
    }



    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long  id) {
        this.id = id;
    }

    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    public void setName(String name) {
        String[] parts = name.split(" ");
        this.firstName = parts[0];
        this.lastName = parts[1];
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", dob='" + dob + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
