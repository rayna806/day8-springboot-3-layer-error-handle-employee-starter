package com.example.demo.mapper;

import com.example.demo.dto.CompanyResponse;
import com.example.demo.entity.Company;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyMapper {

    private final EmployeeMapper employeeMapper;

    public CompanyMapper(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    /**
     * 将Company实体转换为CompanyResponse DTO
     */
    public CompanyResponse toResponse(Company company) {
        if (company == null) {
            return null;
        }
        CompanyResponse companyResponse = new CompanyResponse();
        BeanUtils.copyProperties(company, companyResponse);

        // 转换员工列表
        if (company.getEmployees() != null) {
            companyResponse.setEmployees(employeeMapper.toResponse(company.getEmployees()));
        }

        return companyResponse;
    }

    /**
     * 将Company实体列表转换为CompanyResponse DTO列表
     */
    public List<CompanyResponse> toResponse(List<Company> companies) {
        if (companies == null) {
            return null;
        }
        return companies.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * 将CompanyResponse DTO转换为Company实体
     */
    public Company toEntity(CompanyResponse companyResponse) {
        if (companyResponse == null) {
            return null;
        }
        Company company = new Company();
        BeanUtils.copyProperties(companyResponse, company);

        // 转换员工列表
        if (companyResponse.getEmployees() != null) {
            company.setEmployees(companyResponse.getEmployees().stream()
                    .map(employeeMapper::toEntity)
                    .toList());
        }

        return company;
    }
}
