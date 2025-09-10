package com.example.demo.service;

import com.example.demo.entity.Employee;
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
        return employeeRepository.createEmployee(employee);
    }
    public void deleteEmployee(int id) {
        Employee found = employeeRepository.getEmployeeById(id);
        if(found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        return employeeRepository.updateEmployee(id, updatedEmployee);
    }
}
