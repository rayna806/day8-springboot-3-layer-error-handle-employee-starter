package com.example.demo.repository;

import com.example.demo.entity.Company;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class CompanyRepository {
    private final List<Company> companies = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public void empty() {
        companies.clear();
        idGenerator.set(1);
    }

    public List<Company> getCompanies(Integer page, Integer size) {
        if (page != null && size != null) {
            int start = (page - 1) * size;
            int end = Math.min(start + size, companies.size());
            if (start >= companies.size()) {
                return new ArrayList<>();
            }
            return companies.subList(start, end);
        }
        return new ArrayList<>(companies);
    }

    public Company getCompanyById(int id) {
        return companies.stream()
                .filter(company -> company.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Company createCompany(Company company) {
        company.setId(idGenerator.getAndIncrement());
        companies.add(company);
        return company;
    }

    public Company updateCompany(int id, Company updatedCompany) {
        Company found = getCompanyById(id);
        if (found != null) {
            found.setName(updatedCompany.getName());
            return found;
        }
        return null;
    }

    public boolean deleteCompany(int id) {
        return companies.removeIf(company -> company.getId().equals(id));
    }
}
