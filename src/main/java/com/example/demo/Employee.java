package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    public Employee(String name, Integer age, String gender, Double salary,Integer id) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.gender = gender;
        this.salary = salary;
    }

    public Employee(Integer id) {
        this.id = id;
    }

    private Integer id;
    private String name;
    private Integer age;
    private String gender;
    private Double salary;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public Double getSalary() {
        return salary;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
