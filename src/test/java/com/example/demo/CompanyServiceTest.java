package com.example.demo;

import com.example.demo.entity.Company;
import com.example.demo.repository.ICompanyRepository;
import com.example.demo.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private ICompanyRepository companyRepository;

    @Test
    void should_return_company_when_create_company_with_valid_name() {
        // Given
        Company company = new Company();
        company.setName("Company1");
        Company expectedCompany = new Company();
        expectedCompany.setId(1);
        expectedCompany.setName("Company1");

        when(companyRepository.save(any(Company.class))).thenReturn(expectedCompany);

        // When
        Company result = companyService.createCompany(company);

        // Then
        assertEquals(expectedCompany.getId(), result.getId());
        assertEquals(expectedCompany.getName(), result.getName());
        verify(companyRepository).save(company);
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
        verify(companyRepository, never()).save(any());
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
        verify(companyRepository, never()).save(any());
    }

    @Test
    void should_return_company_when_get_company_by_existing_id() {
        // Given
        int companyId = 1;
        Company expectedCompany = new Company();
        expectedCompany.setId(companyId);
        expectedCompany.setName("Company1");

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(expectedCompany));

        // When
        Company result = companyService.getCompanyById(companyId);

        // Then
        assertEquals(expectedCompany.getId(), result.getId());
        assertEquals(expectedCompany.getName(), result.getName());
        verify(companyRepository).findById(companyId);
    }

    @Test
    void should_throw_exception_when_get_company_by_non_existing_id() {
        // Given
        int companyId = 999;
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> companyService.getCompanyById(companyId));

        assertEquals("Company not found with id: " + companyId, exception.getReason());
        verify(companyRepository).findById(companyId);
    }

    @Test
    void should_return_all_companies_when_get_companies_without_pagination() {
        // Given
        Company company1 = new Company();
        company1.setId(1);
        company1.setName("Company1");

        Company company2 = new Company();
        company2.setId(2);
        company2.setName("Company2");

        List<Company> expectedCompanies = Arrays.asList(company1, company2);
        when(companyRepository.findAll()).thenReturn(expectedCompanies);

        // When
        List<Company> result = companyService.getCompanies(null, null);

        // Then
        assertEquals(2, result.size());
        assertEquals(expectedCompanies, result);
        verify(companyRepository).findAll();
    }

    @Test
    void should_return_paged_companies_when_get_companies_with_page() {
        // Given
        Company company1 = new Company();
        company1.setId(1);
        company1.setName("Company1");

        Company company2 = new Company();
        company2.setId(2);
        company2.setName("Company2");

        List<Company> allCompanies = Arrays.asList(company1, company2);
        when(companyRepository.findAll()).thenReturn(allCompanies);

        // When
        List<Company> result = companyService.getCompanies(1, 1);

        // Then
        assertEquals(1, result.size());
        assertEquals("Company1", result.get(0).getName());
        verify(companyRepository).findAll();
    }

    @Test
    void should_return_updated_company_when_update_existing_company_with_valid_name() {
        // Given
        int companyId = 1;
        Company updatedCompany = new Company();
        updatedCompany.setName("Updated Spring Technologies");

        Company existingCompany = new Company();
        existingCompany.setId(companyId);
        existingCompany.setName("Original Company");

        Company savedCompany = new Company();
        savedCompany.setId(companyId);
        savedCompany.setName("Updated Spring Technologies");

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));
        when(companyRepository.save(updatedCompany)).thenReturn(savedCompany);

        // When
        Company result = companyService.updateCompany(companyId, updatedCompany);

        // Then
        assertEquals(savedCompany.getId(), result.getId());
        assertEquals(savedCompany.getName(), result.getName());
        verify(companyRepository).findById(companyId);
        verify(companyRepository).save(updatedCompany);
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
        verify(companyRepository, never()).findById(anyInt());
        verify(companyRepository, never()).save(any());
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
        verify(companyRepository, never()).findById(anyInt());
        verify(companyRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_update_non_existing_company() {
        // Given
        int companyId = 999;
        Company updatedCompany = new Company();
        updatedCompany.setName("Updated Company");

        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> companyService.updateCompany(companyId, updatedCompany));

        assertEquals("Company not found with id: " + companyId, exception.getReason());
        verify(companyRepository).findById(companyId);
        verify(companyRepository, never()).save(any());
    }

    @Test
    void should_delete_company_when_company_exists() {
        // Given
        int companyId = 1;
        Company existingCompany = new Company();
        existingCompany.setId(companyId);
        existingCompany.setName("Company1");

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));

        // When
        assertDoesNotThrow(() -> companyService.deleteCompany(companyId));

        // Then
        verify(companyRepository).findById(companyId);
        verify(companyRepository).delete(existingCompany);
    }

    @Test
    void should_throw_exception_when_delete_non_existing_company() {
        // Given
        int companyId = 999;
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> companyService.deleteCompany(companyId));

        assertEquals("Company not found with id: " + companyId, exception.getReason());
        verify(companyRepository).findById(companyId);
        verify(companyRepository, never()).delete(any());
    }
}
