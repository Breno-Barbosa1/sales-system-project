package com.breno_barbosa1.sistema_vendas.controller;

import com.breno_barbosa1.sistema_vendas.dto.EmployeeDTO;
import com.breno_barbosa1.sistema_vendas.dto.EmployeeUpdateDTO;
import com.breno_barbosa1.sistema_vendas.exception.EmployeeAlreadyExistsException;
import com.breno_barbosa1.sistema_vendas.exception.EmployeeNotFoundException;
import com.breno_barbosa1.sistema_vendas.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static com.breno_barbosa1.sistema_vendas.common.EmployeeConstants.getValidEmployeeDTO;
import static com.breno_barbosa1.sistema_vendas.common.EmployeeConstants.getValidEmployeeDTOUpdate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void getEmployee_withValidId_ReturnsOk() throws Exception {
        when(employeeService.findById(1L)).thenReturn(getValidEmployeeDTO());

        mockMvc.perform(get("/api/employees/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(getValidEmployeeDTO()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void getEmployee_withValidCpf_ReturnsOk() throws Exception {
        when(employeeService.findByCpf("52998224725")).thenReturn(getValidEmployeeDTO());

        mockMvc.perform(get("/api/employees/cpf/52998224725"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(getValidEmployeeDTO()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void getEmployee_withNonExistingId_ReturnsNotFound() throws Exception {
        when(employeeService.findById(any())).thenThrow(EmployeeNotFoundException.class);

        mockMvc.perform(get("/api/employees/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void getEmployee_withNonExistingCpf_ReturnsNotFound() throws Exception {
        when(employeeService.findByCpf(any())).thenThrow(EmployeeNotFoundException.class);

        mockMvc.perform(get("/api/employees/cpf/99999999999"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void createEmployee_withValidData_ReturnsCreated() throws Exception {
        when(employeeService.create(any(EmployeeDTO.class))).thenReturn(getValidEmployeeDTO());

        mockMvc.perform(post("/api/employees")
            .with(csrf())
            .content(objectMapper.writeValueAsString(getValidEmployeeDTO()))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").value(getValidEmployeeDTO()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void createEmployee_withInvalidData_ReturnsUnprocessableEntity() throws Exception {
        EmployeeDTO employeeDTO = getValidEmployeeDTO();
        employeeDTO.setFirstName("");

        mockMvc.perform(post("/api/employees")
            .with(csrf())
            .content(objectMapper.writeValueAsString(employeeDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableContent())
            .andExpect(jsonPath("$.message").value("Please verify if all fields are set correctly!"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void createEmployee_thatAlreadyExists_ReturnsConflict() throws Exception {
        when(employeeService.create(any())).thenThrow(EmployeeAlreadyExistsException.class);

        mockMvc.perform(post("/api/employees")
            .with(csrf())
            .content(objectMapper.writeValueAsString(getValidEmployeeDTO())).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void updateEmployee_withValidData_ReturnsOk() throws Exception {
        EmployeeUpdateDTO employeeUpdateDTO = getValidEmployeeDTOUpdate();
        employeeUpdateDTO.setFirstName("Breno");
        EmployeeDTO updatedEmployee = getValidEmployeeDTO();
        updatedEmployee.setFirstName(employeeUpdateDTO.getFirstName());

        when(employeeService.update(any(EmployeeUpdateDTO.class))).thenReturn(updatedEmployee);

        mockMvc.perform(put("/api/employees")
            .with(csrf())
            .content(objectMapper.writeValueAsString(employeeUpdateDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(updatedEmployee.getFirstName()))
            .andExpect(jsonPath("$.lastName").value(updatedEmployee.getLastName()))
            .andExpect(jsonPath("$.cpf").value(updatedEmployee.getCpf()))
            .andExpect(jsonPath("$.email").value(updatedEmployee.getEmail()))
            .andExpect(jsonPath("$.address").value(updatedEmployee.getAddress()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void updateEmployee_withInvalidData_ReturnsUnprocessableEntity() throws Exception {
        EmployeeUpdateDTO employeeUpdateDTO = getValidEmployeeDTOUpdate();
        employeeUpdateDTO.setFirstName("");

        mockMvc.perform(put("/api/employees")
            .with(csrf())
            .content(objectMapper.writeValueAsString(employeeUpdateDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableContent())
            .andExpect(jsonPath("$.message").value("Please verify if all fields are set correctly!"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteEmployee_withValidId_ReturnsNoContent() throws Exception {
        doNothing().when(employeeService).delete(1L);

        mockMvc.perform(delete("/api/employees/1")
            .with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteEmployee_withNonExistingId_ReturnsNotFound() throws Exception {
        doThrow(new EmployeeNotFoundException())
            .when(employeeService)
            .delete(99L);

        mockMvc.perform(delete("/api/employees/99")
            .with(csrf()))
            .andExpect(status().isNotFound());
    }
}