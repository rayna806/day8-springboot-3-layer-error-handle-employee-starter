package com.example.demo.dto;

import java.util.List;

public class CompanyResponse {
    private Integer id;
    private String name;
    private List<EmployeeResponse> employees;

    public CompanyResponse() {
    }

    public CompanyResponse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<EmployeeResponse> getEmployees() {
        return employees;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmployees(List<EmployeeResponse> employees) {
        this.employees = employees;
    }
}
