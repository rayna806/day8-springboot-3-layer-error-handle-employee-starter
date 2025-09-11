package com.example.demo.service;

import com.example.demo.entity.Company;
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
        if (company.getName() == null || company.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company name cannot be null or empty");
        }
        return companyRepository.save(company);
    }

    public Company updateCompany(int id, Company updatedCompany) {
        if (updatedCompany.getName() == null || updatedCompany.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company name cannot be null or empty");
        }

        Optional<Company> found = companyRepository.findById(id);
        if (found.isPresent()) {
            updatedCompany.setId(id);
            return companyRepository.save(updatedCompany);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
    }

    public void deleteCompany(int id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            companyRepository.delete(company.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
    }
}
