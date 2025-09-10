package com.example.demo;

import com.example.demo.entity.Company;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    void should_return_company_when_create_company_with_valid_name() {
        // Given
        Company company = new Company();
        company.setName("Company1");
        Company expectedCompany = new Company();
        expectedCompany.setId(1);
        expectedCompany.setName("Company1");

        when(companyRepository.createCompany(any(Company.class))).thenReturn(expectedCompany);

        // When
        Company result = companyService.createCompany(company);

        // Then
        assertEquals(expectedCompany.getId(), result.getId());
        assertEquals(expectedCompany.getName(), result.getName());
        verify(companyRepository).createCompany(company);
    }

    @Test
    void should_throw_exception_when_create_company_with_null_name() {
        // Given
        Company company = new Company();
        company.setName(null);

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> companyService.createCompany(company));

        assertEquals("Company name cannot be null or empty", exception.getReason());
        verify(companyRepository, never()).createCompany(any());
    }

    @Test
    void should_throw_exception_when_create_company_with_empty_name() {
        // Given
        Company company = new Company();
        company.setName("");

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> companyService.createCompany(company));

        assertEquals("Company name cannot be null or empty", exception.getReason());
        verify(companyRepository, never()).createCompany(any());
    }


    @Test
    void should_return_company_when_get_company_by_existing_id() {
        // Given
        int companyId = 1;
        Company expectedCompany = new Company();
        expectedCompany.setId(companyId);
        expectedCompany.setName("Company1");

        when(companyRepository.getCompanyById(companyId)).thenReturn(expectedCompany);

        // When
        Company result = companyService.getCompanyById(companyId);

        // Then
        assertEquals(expectedCompany.getId(), result.getId());
        assertEquals(expectedCompany.getName(), result.getName());
        verify(companyRepository).getCompanyById(companyId);
    }

    @Test
    void should_throw_exception_when_get_company_by_non_existing_id() {
        // Given
        int companyId = 999;
        when(companyRepository.getCompanyById(companyId)).thenReturn(null);

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> companyService.getCompanyById(companyId));

        assertEquals("Company not found with id: " + companyId, exception.getReason());
        verify(companyRepository).getCompanyById(companyId);
    }

    @Test
    void should_return_all_companies_when_get_companies_without_page() {
        // Given
        Company company1 = new Company();
        company1.setId(1);
        company1.setName("Company1");

        Company company2 = new Company();
        company2.setId(2);
        company2.setName("Company2");

        List<Company> expectedCompanies = Arrays.asList(company1, company2);
        when(companyRepository.getCompanies(null, null)).thenReturn(expectedCompanies);

        // When 调用service层的方法，传入null, null,不使用分页
        List<Company> result = companyService.getCompanies(null, null);

        // Then
        assertEquals(2, result.size());
        assertEquals(expectedCompanies, result);
        verify(companyRepository).getCompanies(null, null);// 验证repository的getCompanies方法确实被调用了一次，参数是(null, null)
    }

    @Test
    void should_return_paged_companies_when_get_companies_with_page() {
        // Given
        Company company1 = new Company();
        company1.setId(1);
        company1.setName("Company1");

        List<Company> expectedCompanies = Arrays.asList(company1);
        when(companyRepository.getCompanies(1, 1)).thenReturn(expectedCompanies);

        // When
        List<Company> result = companyService.getCompanies(1, 1);

        // Then
        assertEquals(1, result.size());
        assertEquals(expectedCompanies, result);
        verify(companyRepository).getCompanies(1, 1);
    }

    @Test
    void should_return_updated_company_when_update_existing_company_with_valid_name() {
        // Given
        int companyId = 1;
        Company updatedCompany = new Company();
        updatedCompany.setName("Updated Spring Technologies");

        Company expectedCompany = new Company();
        expectedCompany.setId(companyId);
        expectedCompany.setName("Updated Spring Technologies");

        when(companyRepository.updateCompany(companyId, updatedCompany)).thenReturn(expectedCompany);

        // When
        Company result = companyService.updateCompany(companyId, updatedCompany);

        // Then
        assertEquals(expectedCompany.getId(), result.getId());
        assertEquals(expectedCompany.getName(), result.getName());
        verify(companyRepository).updateCompany(companyId, updatedCompany);
    }

    @Test
    void should_throw_exception_when_update_company_with_null_name() {
        // Given
        int companyId = 1;
        Company updatedCompany = new Company();
        updatedCompany.setName(null);

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> companyService.updateCompany(companyId, updatedCompany));

        assertEquals("Company name cannot be null or empty", exception.getReason());
        verify(companyRepository, never()).updateCompany(anyInt(), any());
    }

    @Test
    void should_throw_exception_when_update_company_with_empty_name() {
        // Given
        int companyId = 1;
        Company updatedCompany = new Company();
        updatedCompany.setName("");

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> companyService.updateCompany(companyId, updatedCompany));

        assertEquals("Company name cannot be null or empty", exception.getReason());
        verify(companyRepository, never()).updateCompany(anyInt(), any());
    }

    @Test
    void should_throw_exception_when_update_non_existing_company() {
        // Given
        int companyId = 999;
        Company updatedCompany = new Company();
        updatedCompany.setName("Updated Company");

        when(companyRepository.updateCompany(companyId, updatedCompany)).thenReturn(null);

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> companyService.updateCompany(companyId, updatedCompany));

        assertEquals("Company not found with id: " + companyId, exception.getReason());
        verify(companyRepository).updateCompany(companyId, updatedCompany);
    }

    @Test
    void should_delete_company_when_company_exists() {
        // Given
        int companyId = 1;
        when(companyRepository.deleteCompany(companyId)).thenReturn(true);

        // When
        assertDoesNotThrow(() -> companyService.deleteCompany(companyId));

        // Then
        verify(companyRepository).deleteCompany(companyId);
    }

    @Test
    void should_throw_exception_when_delete_non_existing_company() {
        // Given
        int companyId = 999;
        when(companyRepository.deleteCompany(companyId)).thenReturn(false);

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> companyService.deleteCompany(companyId));

        assertEquals("Company not found with id: " + companyId, exception.getReason());
        verify(companyRepository).deleteCompany(companyId);
    }

    @Test
    void should_call_repository_empty_when_empty_is_called() {
        // When
        companyService.empty();

        // Then
        verify(companyRepository).empty();
    }
}
