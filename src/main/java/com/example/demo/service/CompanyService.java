package com.example.demo.service;

import com.example.demo.entity.Company;
import com.example.demo.entity.Employee;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.ICompanyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final ICompanyRepository companyRepository;

    public CompanyService(ICompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

//    public void empty() {
//        this.companyRepository.empty();
//    }

    public List<Company> getCompanies(Integer page, Integer size) {
        if (page == null || size == null) {
            return companyRepository.findAll();
        } else {
            int start = (page - 1) * size;
            return companyRepository.findAll().stream().skip(start).limit(size).toList();
        }
    }

    public Company getCompanyById(int id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        return company.get();
    }

    public Company createCompany(Company company) {
        // Add business logic validation here if needed
        if (company.getName() == null || company.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company name cannot be null or empty");
        }
        return companyRepository.save(company);
    }

    public Company updateCompany(int id, Company updatedCompany) {
        // Add business logic validation here if needed
        if (updatedCompany.getName() == null || updatedCompany.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company name cannot be null or empty");
        }

        Optional<Company> found = companyRepository.findById(id);
        if (found.isPresent()) {
            updatedCompany.setId(id);  // 确保设置正确的ID
            return companyRepository.save(updatedCompany);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
    }

    public void deleteCompany(int id) {
        boolean deleted = companyRepository.findById(id).map(company -> {;
            companyRepository.delete(company);
            return true;
        }).orElse(false);
        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
    }
}
