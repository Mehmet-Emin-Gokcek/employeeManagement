package com.api.employeeManagement;
import com.api.employeeManagement.payroll.Employee.EmployeeNotFoundException;
import com.api.employeeManagement.payroll.Employee.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.api.employeeManagement.payroll.Employee.Employee;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeManagementApplicationTests {
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService mockService;

    @BeforeEach
    public void init() throws EmployeeNotFoundException {
        Employee employee = new Employee(
                1L,
                "John",
                "Denver",
                "Manager",
                "john@gmail.com",
                "04/15/1970",
                "4325 NW 83rd st. Houston, TX",
                "(816)555-5656");

        when(mockService.getEmployee(1L)).thenReturn(employee);
    }

    @Test
    public void find_employeeId_OK() throws Exception {
        mockMvc.perform(get("/api/employees/1"))
                .andDo(print())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.role", is("Manager")))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Denver")))
                .andExpect(jsonPath("$.email", is("john@gmail.com")))
                .andExpect(jsonPath("$.dob", is("04/15/1970")))
                .andExpect(jsonPath("$.address", is("4325 NW 83rd st. Houston, TX")))
                .andExpect(jsonPath("$.phone", is("(816)555-5656")));

        verify(mockService, times(1)).getEmployee(1L);
    }

    @Test
    public void find_allEmployee_OK() throws Exception {

        List<Employee> employees = Arrays.asList(
                new Employee(
                        1L,
                        "Marcus",
                        "Levis",
                        "Tech Lead",
                        "marcus@gmail.com",
                        "03/03/1979",
                        "5543 Tech Ave. San Fransisco, CA",
                        "(540)665-2343"),

                new Employee(
                        2L,
                        "Sarah",
                        "Jones",
                        "Staff",
                        "sarah@gmail.com",
                        "02/28/1981",
                        "65545 Pennsylvania Avenue, PA",
                        "(675)554-3433"));

        when(mockService.getEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.employeeList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.employeeList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.employeeList[0].role", is("Tech Lead")))
                .andExpect(jsonPath("$._embedded.employeeList[0].firstName", is("Marcus")))
                .andExpect(jsonPath("$._embedded.employeeList[0].lastName", is("Levis")))
                .andExpect(jsonPath("$._embedded.employeeList[0].email", is("marcus@gmail.com")))
                .andExpect(jsonPath("$._embedded.employeeList[0].dob", is("03/03/1979")))
                .andExpect(jsonPath("$._embedded.employeeList[0].address", is("5543 Tech Ave. San Fransisco, CA")))
                .andExpect(jsonPath("$._embedded.employeeList[0].phone", is("(540)665-2343")))

                .andExpect(jsonPath("$._embedded.employeeList[1].id", is(2)))
                .andExpect(jsonPath("$._embedded.employeeList[1].role", is("Staff")))
                .andExpect(jsonPath("$._embedded.employeeList[1].firstName", is("Sarah")))
                .andExpect(jsonPath("$._embedded.employeeList[1].lastName", is("Jones")))
                .andExpect(jsonPath("$._embedded.employeeList[1].email", is("sarah@gmail.com")))
                .andExpect(jsonPath("$._embedded.employeeList[1].dob", is("02/28/1981")))
                .andExpect(jsonPath("$._embedded.employeeList[1].address", is("65545 Pennsylvania Avenue, PA")))
                .andExpect(jsonPath("$._embedded.employeeList[1].phone", is("(675)554-3433")));

        verify(mockService, times(1)).getEmployees();
    }


    @Test
    public void find_employeeIdNotFound_404() throws Exception {
        mockMvc.perform(get("/api/test"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void save_employee_OK() throws Exception {
        Employee newEmployee = new Employee(
                1L,
                "Jane",
                "Doe",
                "Staff",
                "jane@gmail.com",
                "01/01/1983",
                "75000 Main St. Burlington, NJ",
                "(755)455-1222");
        when(mockService.addEmployee(any(Employee.class))).thenReturn(newEmployee);

        mockMvc.perform(post("/api/employees")
                .content(om.writeValueAsString(newEmployee))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.role", is("Staff")))
                .andExpect(jsonPath("$.firstName", is("Jane")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("jane@gmail.com")))
                .andExpect(jsonPath("$.dob", is("01/01/1983")))
                .andExpect(jsonPath("$.address", is("75000 Main St. Burlington, NJ")))
                .andExpect(jsonPath("$.phone", is("(755)455-1222")));

        verify(mockService, times(1)).addEmployee(any(Employee.class));
    }


//    @Test
//    public void update_employee_OK() throws Exception {
//
//     Employee updateEmployee = new Employee(1L, "", "", "","","");
//     when(mockService.save(any(Employee.class))).thenReturn(updateEmployee);
//
//     mockMvc.perform(put("/employees/1")
//             .content(om.writeValueAsString(updateEmployee))
//             .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
//             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.id", is(1)))
//             .andExpect(jsonPath("$.name", is("")))
//             .andExpect(jsonPath("$.email", is("")))
//             .andExpect(jsonPath("$.dob", is("")))
//             .andExpect(jsonPath("$.address", is("")))
//             .andExpect(jsonPath("$.phone", is("")));
// }



// @Test
// public void delete_employee_OK() throws Exception {
//
//     doNothing().when(mockService).deleteById(1L);
//
//     mockMvc.perform(delete("/employees/1"))
//             .andExpect(status().isOk());
//
//    verify(mockService, times(1)).deleteById(1L);
//}

//    private static void printJSON(Object object) {
//        String result;
//        try {
//            result = om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
//            System.out.println(result);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//    }
}
