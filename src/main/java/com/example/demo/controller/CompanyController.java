package com.example.demo.controller;

import com.example.demo.dto.CompanyResponse;
import com.example.demo.entity.Company;
import com.example.demo.mapper.CompanyMapper;
import com.example.demo.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    public CompanyController(CompanyService companyService, CompanyMapper companyMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }

    @GetMapping
    public List<CompanyResponse> getCompanies(@RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer size) {
        return companyMapper.toResponse(companyService.getCompanies(page, size));
    }

    @GetMapping("/{id}")
    public CompanyResponse getCompanyById(@PathVariable int id) {
        return companyMapper.toResponse(companyService.getCompanyById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse createCompany(@RequestBody Company company) {
        return companyMapper.toResponse(companyService.createCompany(company));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompanyResponse updateCompany(@PathVariable int id, @RequestBody Company updatedCompany) {
        return companyMapper.toResponse(companyService.updateCompany(id, updatedCompany));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable int id) {
        companyService.deleteCompany(id);
    }
}
