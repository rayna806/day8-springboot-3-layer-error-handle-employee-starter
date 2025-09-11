package com.example.demo.mapper;

import com.example.demo.dto.EmployeeRequest;
import com.example.demo.dto.EmployeeResponse;
import com.example.demo.entity.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeMapper {

    /**
     * 将Employee实体转换为EmployeeResponse DTO
     */
    public EmployeeResponse toResponse(Employee employee) {
        if (employee == null) {
            return null;
        }
        EmployeeResponse employeeResponse = new EmployeeResponse();
        BeanUtils.copyProperties(employee, employeeResponse);
        return employeeResponse;
    }

    /**
     * 将Employee实体列表转换为EmployeeResponse DTO列表
     */
    public List<EmployeeResponse> toResponse(List<Employee> employees) {
        if (employees == null) {
            return null;
        }
        return employees.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * 将EmployeeRequest DTO转换为Employee实体
     */
    public Employee toEntity(EmployeeRequest employeeRequest) {
        if (employeeRequest == null) {
            return null;
        }
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeRequest, employee);
        return employee;
    }

    /**
     * 将EmployeeResponse DTO转换为Employee实体
     */
    public Employee toEntity(EmployeeResponse employeeRequest) {
        if (employeeRequest == null) {
            return null;
        }
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeRequest, employee);
        return employee;
    }
}
