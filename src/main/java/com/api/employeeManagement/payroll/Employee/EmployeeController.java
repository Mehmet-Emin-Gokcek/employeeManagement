package com.api.employeeManagement.payroll.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@CrossOrigin(origins = "https://mehmetgokcek.github.io", maxAge = 3600)
@RestController
@RequestMapping(path = "api/employees")
public class EmployeeController {

    private final EmployeeService service;
    private final EmployeeModelAssembler assembler;


    //@Autowired: tells the spring to instantiate studentService which is a spring component/spring bean
    // and inject into the studentController as a dependency (e.i. dependency injection)
    @Autowired
    public EmployeeController(EmployeeService service, EmployeeModelAssembler assembler){
        this.service = service;
        this.assembler = assembler;
    }


    // api/employees
    @GetMapping
    public CollectionModel<EntityModel<Employee>> getEmployees(){

        List<EntityModel<Employee>> employees = service.getEmployees().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).getEmployees()).withSelfRel());
    }

    // api/employees/id
    @GetMapping(path = "{employeeId}")
    public ResponseEntity<?> getEmployee(@PathVariable("employeeId") Long employeeId){

        EntityModel<Employee> entityModel = assembler.toModel(service.getEmployee(employeeId));

        return ResponseEntity.ok(entityModel);

    }


    // api/employees
    @PostMapping
    public ResponseEntity<?> addEmployee(@RequestBody Employee newEmployee) {
        EntityModel<Employee> entityModel = assembler.toModel(service.addEmployee(newEmployee));
        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }


    // api/employees/id
    @DeleteMapping(path = "{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("employeeId") Long employeeId){
        service.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }


    // api/employees/id
    @PutMapping(path = "{employeeId}")
    public ResponseEntity<?> updateEmployee(@RequestBody Employee employee, @PathVariable("employeeId") Long employeeId){
        Employee updatedEmployee = service.updateEmployee(employee, employeeId);
        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    /*/ api/employees/id?name=Maria&email=mariam.jamal@gmail.com
    @PutMapping(path="{employeeId}")
    public void updateEmployee(
            @PathVariable("employeeId") Long employeeId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        service.updateEmployee(employeeId, name, email);

    }*/
}

