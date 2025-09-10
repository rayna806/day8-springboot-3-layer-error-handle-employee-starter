package com.example.demo.repository;

import com.example.demo.entity.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Repository
public class EmployeeRepository {
    private final List<Employee> employees = new ArrayList<>();

    public void empty() {
        employees.clear();
    }
    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        Stream<Employee> stream = employees.stream();
        if (gender != null) {
            stream = stream.filter(employee -> employee.getGender().compareToIgnoreCase(gender) == 0);
        }
        if (page != null && size != null) {
            stream = stream.skip((long) (page - 1) * size).limit(size);
        }
        return stream.toList();
    }
    public Employee getEmployeeById(int id) {
        return employees.stream()
                .filter(employee -> employee.getId() == id)
                .findFirst()
                .orElse(null);
    }
    public Employee createEmployee(Employee employee) {
        employee.setId(employees.size() + 1);
        employees.add(employee);
        return employee;
    }

    public Employee deleteEmployee(int id) {
        Employee found = getEmployeeById(id);
        if (found == null) {
            return null;
        }
        found.setStatus(false);
        employees.remove(found);
        return found;
    }
    public Employee updateEmployee( int id, Employee updatedEmployee) {
        Employee found = null;
        //Employee found = getEmployeeById(id);
        for (Employee e : employees) {
            if (Objects.equals(e.getId(), id)) {
                found = e;
                break;
            }
        }
        if (found == null) {
            return null;
        }
        found.setName(updatedEmployee.getName());
        found.setAge(updatedEmployee.getAge());
        found.setGender(updatedEmployee.getGender());
        found.setSalary(updatedEmployee.getSalary());
        return found;
    }

}