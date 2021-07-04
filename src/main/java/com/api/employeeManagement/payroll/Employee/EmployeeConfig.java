package com.api.employeeManagement.payroll.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

@Configuration
public class EmployeeConfig {

    private static final Logger log = LoggerFactory.getLogger(EmployeeConfig.class);


    @Bean
    CommandLineRunner employeeCommandLineRunner (EmployeeRepository repository)
    {
        return args -> {
            ArrayList<Employee> employees = new ArrayList<>();

            employees.add(new Employee(
                    "Manager",
                    "Mariam",
                    "Jamal",
                    "mariam.jamal@gmail.com",
                    "06/12/2000",
                    "3223 42st. St. Houston, TX",
                    "(832)665-6756"
                   ));
            employees.add(new Employee(
                    "Scrum Master",
                    "Alex",
                    "Gorgonza",
                    "alex@gmail.com",
                    "03/03/2001",
                    "3323 55th St. Waco, TX",
                    "(445)677-3443"

            ));

            employees.add(new Employee(
                    "Staff",
                    "John",
                    "Denver",
                    "john@gmail.com",
                    "01/21/1980",
                    "347 NW 85th Ave. Philadelphia,PA",
                    "(743)554-3267"
            ));

            //check to see if email provided already exists in the database
            //each employee must have unique email address
            Consumer<Employee> saveEmployee = new Consumer<Employee>() {
                @Override
                public void accept(Employee employee) {
                    Optional<Employee> employeeOptional = repository.findEmployeebyEmail(employee.getEmail());
                    if(!employeeOptional.isPresent()) {
                        repository.save(employee);
                    }
                }
            };
            employees.forEach(saveEmployee);
        };
    }
}
