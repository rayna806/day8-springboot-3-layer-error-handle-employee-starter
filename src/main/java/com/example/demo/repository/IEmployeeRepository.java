package com.example.demo.repository;

import com.example.demo.entity.Employee;
import org.springframework.data.domain.ManagedTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IEmployeeRepository extends JpaRepository<Employee, Integer> {


    List<Employee> findEmployeesByGender(String gender);

    Page<Employee> findEmployeesByGender(String gender, Pageable pageable);



}

