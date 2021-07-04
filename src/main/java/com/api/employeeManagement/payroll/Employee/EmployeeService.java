package com.api.employeeManagement.payroll.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//@service: means service class will serve as a spring component (e.i. a spring bean)
//that will be used at student controller
@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    @Autowired
    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }


    public List<Employee> getEmployees(){
        return repository.findAll();
    }

    public Employee addEmployee(Employee newEmployee) {
       Optional<Employee> employeeOptional =  repository.findEmployeebyEmail(newEmployee.getEmail());

       if(employeeOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }

        return repository.save(newEmployee);
    }

    public void deleteEmployee(Long employeeId) {
        boolean exists = repository.existsById(employeeId);
        if(!exists) {
            throw new IllegalStateException(
                    "employee with id " + employeeId + " does not exist");
        }
        repository.deleteById(employeeId);
    }

    public Employee getEmployee(Long employeeId) {

        return repository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }

    public Employee updateEmployee(Employee newEmployee, Long employeeId) {

        return repository.findById(employeeId)
                .map(employee -> {
                    employee.setFirstName(newEmployee.getFirstName());
                    employee.setLastName(newEmployee.getLastName());
                    employee.setRole(newEmployee.getRole());
                    employee.setEmail(newEmployee.getEmail());
                    employee.setDob(newEmployee.getDob());
                    employee.setAddress(newEmployee.getAddress());
                    employee.setPhone(newEmployee.getPhone());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(employeeId);
                    return repository.save(newEmployee);
                });
    }


   /* public void updateEmployee(Employee employee, Long employeeId) {
        //check to see if an employee with 'employeeId' exists
        Employee employeeToUpdate = repository.getById(employeeId);
        if(employeeToUpdate == null) {
            throw new IllegalStateException(
                    "employee with id " + employeeId + " does not exist");
        }
        //check to see if email provided already exists in the database
        //each employee must have unique email address
        Optional<Employee> employeeOptional =  repository.findEmployeebyEmail(employee.getEmail());
        if(employeeOptional.isPresent() && !Objects.equals(employee.getEmail(), employeeToUpdate.getEmail())) {
            throw new IllegalStateException("email taken");
        }

        //check to see if provided name is null or empty
        if(employee.getName() != null && employee.getName() .length() > 0 ) {
            employeeToUpdate.setName(employee.getName());
        }
        //check to see if provided email is null or empty
        if(employee.getEmail() != null && employee.getEmail() .length() > 0) {
            employeeToUpdate.setEmail(employee.getEmail());
        }

        //check to see if provided DOB is null or empty
        if(employee.getDob() != null && employee.getDob() .length() > 0 ) {
            employeeToUpdate.setDob(employee.getDob());
        }
        //check to see if provided Address is null or empty
        if(employee.getAddress() != null && employee.getAddress() .length() > 0) {
            employeeToUpdate.setAddress(employee.getAddress());
        }

        //check to see if provided Phone is null or empty
        if(employee.getPhone() != null && employee.getPhone() .length() > 0) {
            employeeToUpdate.setPhone(employee.getPhone());
        }

        repository.save(employeeToUpdate);
    }
*/
    //@Transactional: Entity goes into manage state
    //So, we don't need to invoke any of the repository methods to update the employee object
 /*   @Transactional
    public void updateEmployee(Long employeeId, String name, String email) {

        //check to see if employee with 'employeeId' exists or throw exception
        Employee employee = repository.findById(employeeId)
                .orElseThrow(() -> new IllegalStateException(
                        "employee with id " + employeeId + " does not exist" ));

        //check to see if name provided is empty/null and isn't the same as the current name in the database
        if(name != null && name.length() > 0 && !Objects.equals(employee.getName(), name) ){
            employee.setName(name);
        }

        //check to see if email provided is empty/null and isn't the same as the current email in the database
        if(email != null && email.length() > 0 && !Objects.equals(employee.getEmail(), email) ){

            //check to see if email provided already exists in the database
            //each employee must have unique email address
            Optional<Employee> employeeOptional = repository.findEmployeebyEmail(email);
            if(employeeOptional.isPresent()){
                throw  new IllegalStateException("email taken");
            }
            employee.setEmail(email);
        }
    }*/
}
