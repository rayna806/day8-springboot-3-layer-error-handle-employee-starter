package com.example.demo;

import com.example.demo.controller.CompanyController;
import com.example.demo.entity.Company;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyController companyController;

    private Company createSpringCompany() throws Exception {
        Gson gson = new Gson();
        Company spring = new Company();
        spring.setName("Spring Technologies");
        String springString = gson.toJson(spring);

        String contentAsString = mockMvc.perform(post("/companies")
                .contentType(MediaType.APPLICATION_JSON).content(springString))
                .andReturn().getResponse().getContentAsString();
        spring.setId(gson.fromJson(contentAsString, Company.class).getId());
        return spring;
    }

    private Company createOracleCompany() throws Exception {
        Gson gson = new Gson();
        Company oracle = new Company();
        oracle.setName("Oracle Corporation");
        String oracleString = gson.toJson(oracle);

        String contentAsString = mockMvc.perform(post("/companies")
                .contentType(MediaType.APPLICATION_JSON).content(oracleString))
                .andReturn().getResponse().getContentAsString();
        oracle.setId(gson.fromJson(contentAsString, Company.class).getId());
        return oracle;
    }

    @BeforeEach
    void cleanCompanies() {
        companyController.empty();
    }

    @Test
    void should_return_404_when_company_not_found() throws Exception {
        mockMvc.perform(get("/companies/999").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_all_companies() throws Exception {
        createSpringCompany();
        createOracleCompany();
        mockMvc.perform(get("/companies").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_return_company_when_company_found() throws Exception {
        createSpringCompany();

        mockMvc.perform(get("/companies/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Spring Technologies"));
    }

    @Test
    void should_create_company() throws Exception {
        String requestBody = """
                {
                    "name": "Spring Technologies"
                }
                """;

        mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Spring Technologies"));
    }

    // ExceptionHandler - 模仿Employee的异常测试
    @Test
    void should_create_company_name_is_null() throws Exception {
        String requestBody = """
                {
                }
                """;

        mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(jsonPath("$.message").value("company name is null!"));
    }

    // ExceptionHandler - 模仿Employee的异常测试
    @Test
    void should_create_company_name_is_empty() throws Exception {
        String requestBody = """
                {
                    "name": ""
                }
                """;

        mockMvc.perform(post("/companies").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(jsonPath("$.message").value("company name cannot be empty!"));
    }

    // ExceptionHandler - 模仿Employee的异常测试
    @Test
    void should_update_company_name_is_null() throws Exception {
        Company company = createSpringCompany();
        String requestBody = """
                {
                }
                """;

        mockMvc.perform(put("/companies/" + company.getId()).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(jsonPath("$.message").value("company name is null!"));
    }

    // ExceptionHandler - 模仿Employee的异常测试
    @Test
    void should_update_company_name_is_empty() throws Exception {
        Company company = createSpringCompany();
        String requestBody = """
                {
                    "name": ""
                }
                """;

        mockMvc.perform(put("/companies/" + company.getId()).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(jsonPath("$.message").value("company name cannot be empty!"));
    }

    @Test
    void should_return_200_with_empty_body_when_no_companies() throws Exception {
        mockMvc.perform(get("/companies").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void should_return_200_with_company_list() throws Exception {
        Company expect = createSpringCompany();

        mockMvc.perform(get("/companies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(expect.getId()))
                .andExpect(jsonPath("$[0].name").value(expect.getName()));
    }

    @Test
    void should_status_204_when_delete_company() throws Exception {
        int id = createSpringCompany().getId();

        mockMvc.perform(delete("/companies/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_status_404_when_delete_non_existent_company() throws Exception {
        mockMvc.perform(delete("/companies/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_status_200_when_update_company() throws Exception {
        Company expect = createSpringCompany();
        String requestBody = """
                {
                    "name": "Spring Framework Inc"
                }
                """;

        mockMvc.perform(put("/companies/" + expect.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expect.getId()))
                .andExpect(jsonPath("$.name").value("Spring Framework Inc"));
    }

    @Test
    void should_status_404_when_update_non_existent_company() throws Exception {
        String requestBody = """
                {
                    "name": "Updated Company"
                }
                """;

        mockMvc.perform(put("/companies/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_status_200_and_return_paged_company_list() throws Exception {
        createSpringCompany();
        createOracleCompany();
        createSpringCompany();
        createOracleCompany();
        createSpringCompany();
        createOracleCompany();

        mockMvc.perform(get("/companies?page=1&size=5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void should_return_empty_list_when_page_out_of_range() throws Exception {
        createSpringCompany();
        createOracleCompany();

        mockMvc.perform(get("/companies?page=5&size=5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
