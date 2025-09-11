package com.example.demo.repository;

import com.example.demo.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICompanyRepository extends JpaRepository<Company, Integer> {

    // 按公司名称查询（用于业务验证，避免重复公司名）
    Optional<Company> findByName(String name);
}
