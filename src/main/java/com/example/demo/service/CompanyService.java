package com.example.demo.service;

import com.example.demo.entity.Company;
import com.example.demo.repository.CompanyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void empty() {
        this.companyRepository.empty();
    }

    public List<Company> getCompanies(Integer page, Integer size) {
        return companyRepository.getCompanies(page, size);
    }

    public Company getCompanyById(int id) {
        Company company = companyRepository.getCompanyById(id);
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        return company;
    }

    public Company createCompany(Company company) {
        // Add business logic validation here if needed
        if (company.getName() == null || company.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company name cannot be null or empty");
        }
        return companyRepository.createCompany(company);
    }

    public Company updateCompany(int id, Company updatedCompany) {
        // Add business logic validation here if needed
        if (updatedCompany.getName() == null || updatedCompany.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company name cannot be null or empty");
        }

        Company found = companyRepository.updateCompany(id, updatedCompany);
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        return found;
    }

    public void deleteCompany(int id) {
        boolean deleted = companyRepository.deleteCompany(id);
        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
    }
}
