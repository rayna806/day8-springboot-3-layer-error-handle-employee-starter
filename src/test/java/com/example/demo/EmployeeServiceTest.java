package com.example.demo;

import com.example.demo.entity.Employee;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidSalaryEmployeeException;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.IEmployeeRepository;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;
    @Mock
    private IEmployeeRepository employeeRepository;

    @Test
    void should_return_employee_when_create_a_employee_with_valid_age() {
        Employee employee = new Employee(null, "John Smith", 20, "MALE", 5000.0);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        Employee employeeResult = employeeService.createEmployee(employee);
        assertEquals(employeeResult.getAge(),employee.getAge());
    }

    @Test
    void should_throw_expection_when_create_a_employee_of_greater_than_65_or_less_than_18() {
        Employee employee = new Employee(null, "John Smith", 10, "MALE", 5000.0);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        assertThrows(InvalidAgeEmployeeException.class,() -> employeeService.createEmployee(employee));
    }

    @Test
    void should_throw_expection_when_create_a_employee_of_greater_than_30_with_salary_below_20000() {
        Employee employee = new Employee(null, "John Smith", 31, "MALE", 15000.0);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        assertThrows(InvalidSalaryEmployeeException.class, () -> employeeService.createEmployee(employee));
    }

    @Test
    void should_set_employee_status_active_by_default_when_create_employee() {
        Employee employee = new Employee(null, "John Smith", 31, "MALE", 15000.0);
        //employee.setStatus(true);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        assertEquals(true, employee.getStatus());
    }

    @Test
    void should_throw_exception_when_update_a_employee_already_left_the_company(){
        Employee employee = new Employee(1, "John Smith", 31, "MALE", 15000.0);
        assertTrue(employee.getStatus());
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        employeeService.deleteEmployee(1);
        verify(employeeRepository).save(argThat(e -> !e.getStatus()));
    }





//        Employee employee = new Employee(1, "John Smith", 31, "MALE", 15000.0);
//        employee.setStatus(false);
//        employeeRepository.deleteEmployee(employee.getId());
//        String requestBody= """
//                {
//                    "name": "John Smith",
//                    "age": 31,
//                    "gender": "Male",
//                    "salary": 15000.0
//                }
//               """;
//        when(employeeRepository.getEmployeeById(employee.getId())).thenReturn(employee);
//        assertThrows(RuntimeException.class, () -> employeeService.updateEmployee(employee.getId(),employee));







}
