package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidSalaryEmployeeException;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public void empty() {
        this.employeeRepository.empty();
    }
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        return employeeRepository.getEmployees(gender, page, size);
    }

    public Employee getEmployeeById(int id) {
        Employee employee = employeeRepository.getEmployeeById(id);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employee;
    }

    public Employee createEmployee(Employee employee) {
        if(employee.getAge()==null){
            throw new InvalidAgeEmployeeException("employee age is null!");
        }
        if(employee.getAge()>65||employee.getAge()<18){
            throw new InvalidAgeEmployeeException("employee age is out of range!");
        }
        if(employee.getAge()>=30&&employee.getSalary()<20000){
            throw new InvalidSalaryEmployeeException("employee salary cannot be below 20000 if age is greater than 30!");
        }
        return employeeRepository.createEmployee(employee);
    }

    public void deleteEmployee(int id) {
        Employee found = employeeRepository.getEmployeeById(id);
        if(found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        found.setStatus(false);
        employeeRepository.updateEmployee(id,found);

    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        Employee found = employeeRepository.getEmployeeById(id);
        if(found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        if (!found.getStatus()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't update employee that ha`s been deleted with id: " + id);
        }
        return employeeRepository.updateEmployee(id, updatedEmployee);
    }
}
