package com.example.demo;

import com.example.demo.controller.EmployeeController;
import com.example.demo.entity.Employee;
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
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeController employeeController;

    //    private static Employee employee(String name, int age, String gender, double salary) {
//        Employee e = new Employee();
//        e.setName(name);
//        e.setAge(age);
//        e.setGender(gender);
//        e.setSalary(salary);
//        return e;
//    }
//
    private Employee createJohnSmith() throws Exception {
        Gson gson = new Gson();
        Employee john = new Employee(null, "John Smith", 28, "MALE", 60000.0);
        String johnString = gson.toJson(john).toString();

        String contentAsString = mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON).content(johnString)).andReturn().getResponse().getContentAsString();
        john.setId(gson.fromJson(contentAsString, Employee.class).getId());
        return john;
    }

    private Employee createJaneDoe() throws Exception {
        Gson gson = new Gson();
        Employee jane = new Employee(null, "Jane Doe", 22, "FEMALE", 60000.0);
        String janeString = gson.toJson(jane).toString();

        String contentAsStringJane = mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON).content(janeString)).andReturn().getResponse().getContentAsString();
        jane.setId(gson.fromJson(contentAsStringJane, Employee.class).getId());
        return jane;
    }

    @BeforeEach
    void cleanEmployees() throws Exception {
        mockMvc.perform(delete("/employees/all"));
    }

    @Test
    void should_return_404_when_employee_not_found() throws Exception {
        mockMvc.perform(get("/employees/999").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }
//
    @Test
    void should_return_all_employee() throws Exception {
//        employeeController.createEmployee(johnSmith());
//        employeeController.createEmployee(janeDoe());
        createJohnSmith();
        createJaneDoe();
        mockMvc.perform(get("/employees").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
//        mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(jane));
//        mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(john));

    }

    @Test
    void should_return_employee_when_employee_found() throws Exception {
        createJohnSmith();

        mockMvc.perform(get("/employees/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Smith")).andExpect(jsonPath("$.age").value(28)).andExpect(jsonPath("$.gender").value("MALE")).andExpect(jsonPath("$.salary").value(60000));
    }

    @Test
    void should_return_male_employee_when_employee_found() throws Exception {
        Employee expect = createJohnSmith();
        employeeController.createEmployee(createJohnSmith()); // Change to use createJaneDoe()

        mockMvc.perform(get("/employees?gender=male")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expect.getId()))
                .andExpect(jsonPath("$[0].name").value(expect.getName()))
                .andExpect(jsonPath("$[0].age").value(expect.getAge()))
                .andExpect(jsonPath("$[0].gender").value(expect.getGender()))
                .andExpect(jsonPath("$[0].salary").value(expect.getSalary()));
    }

    @Test
    void should_create_employee() throws Exception {
        String requestBody = """
                        {
                            "name": "John Smith",
                            "age": 28,
                            "gender": "MALE",
                            "salary": 60000
                        }
                """;

        mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isCreated()).andExpect(jsonPath("$.id").isNumber()).andExpect(jsonPath("$.name").value("John Smith")).andExpect(jsonPath("$.age").value(28)).andExpect(jsonPath("$.gender").value("MALE")).andExpect(jsonPath("$.salary").value(60000));
    }

    @Test
    void should_return_200_with_empty_body_when_no_employee() throws Exception {
        mockMvc.perform(get("/employees").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
    }


    @Test
    void should_return_200_with_employee_list() throws Exception {
        Employee expect = createJohnSmith();

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expect.getId()))
                .andExpect(jsonPath("$[0].name").value(expect.getName()))
                .andExpect(jsonPath("$[0].age").value(expect.getAge()))
                .andExpect(jsonPath("$[0].gender").value(expect.getGender()))
                .andExpect(jsonPath("$[0].salary").value(expect.getSalary()));
    }

    @Test
    void should_status_204_when_delete_employee() throws Exception {
        int id = createJohnSmith().getId();

        mockMvc.perform(delete("/employees/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_status_200_when_update_employee() throws Exception {
        Employee expect = createJohnSmith();
        String requestBody = """
                        {
                            "name": "John Smith",
                            "age": 29,
                            "gender": "MALE",
                            "salary": 65000.0
                        }
                """;

        mockMvc.perform(put("/employees/" + expect.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expect.getId()))
                .andExpect(jsonPath("$.age").value(29))
                .andExpect(jsonPath("$.salary").value(65000.0));
    }

    @Test
    void should_status_200_and_return_paged_employee_list() throws Exception {
        createJohnSmith();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();

        mockMvc.perform(get("/employees?page=1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }


}
