package com.example.demo.controller;

import com.example.demo.dto.EmployeeRequest;
import com.example.demo.dto.EmployeeResponse;
import com.example.demo.entity.Employee;
import com.example.demo.mapper.EmployeeMapper;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper; // Added EmployeeMapper declaration
    public EmployeeController(EmployeeService employeeService, EmployeeMapper e) {
        this.employeeService = employeeService;
        this.employeeMapper = e;
    }

    //
    @GetMapping
    public List<EmployeeResponse> getEmployees(@RequestParam(required = false) String gender, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return employeeMapper.toResponse(employeeService.getEmployees(gender, page, size));
    }


    //
    @GetMapping("/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable int id) {
       return employeeMapper.toResponse(employeeService.getEmployeeById(id));
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse updateEmployee(@PathVariable int id, @RequestBody EmployeeRequest updatedEmployee) {
        return employeeMapper.toResponse(employeeService.updateEmployee(id, updatedEmployee));
    }

    //
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
    }

//    @DeleteMapping("/all")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void empty() {
//        employeeService.empty();
//    }
    //
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse createEmployee(@RequestBody @Validated EmployeeRequest employee) {

        Employee employeeEntity = employeeMapper.toEntity(employee);
        return employeeMapper.toResponse(employeeService.createEmployee(employeeEntity));
    }


}
