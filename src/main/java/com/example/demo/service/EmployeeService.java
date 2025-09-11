package com.example.demo.service;

import com.example.demo.dto.EmployeeRequest;
import com.example.demo.entity.Employee;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidSalaryEmployeeException;
import com.example.demo.mapper.EmployeeMapper;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.IEmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeService {

    private final IEmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper = new EmployeeMapper();

//    public void empty() {
//        this.employeeRepository.empty();
//    }
    public EmployeeService(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    //
    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        if(gender == null){
            if(page == null || size == null){
                return employeeRepository.findAll();
            } else{
                Pageable pageable = PageRequest.of(page-1, size);
                return employeeRepository.findAll(pageable).toList();
            }

        }else{
            if(page == null || size == null){
                return employeeRepository.findEmployeesByGender(gender);
            }else{
                Pageable pageable = PageRequest.of(page-1, size);
                return employeeRepository.findEmployeesByGender(gender, pageable).stream().toList();
            }
        }

    }

    //
    public Employee getEmployeeById(int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " " + id);
        }
        return employee.get();
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
        //
        //return employeeRepository.createEmployee(employee);
        return employeeRepository.save(employee);

    }

    public void deleteEmployee(int id) {
        Optional<Employee> found = employeeRepository.findById(id);
        if(found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        found.get().setStatus(false);
        employeeRepository.save(found.get());

    }

    public Employee updateEmployee(int id, EmployeeRequest updatedEmployee) {
        Employee employee = employeeMapper.toEntity(updatedEmployee);
        Optional<Employee> found = employeeRepository.findById(id);
        if(found.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        if (!found.get().getStatus()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't update employee that ha`s been deleted with id: " + id);
        }
        employee.setId(id);
        return employeeRepository.save(employee);
    }
}
