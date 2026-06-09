package com.breno_barbosa1.sistema_vendas.controller;

import com.breno_barbosa1.sistema_vendas.dto.CreateSaleDTO;
import com.breno_barbosa1.sistema_vendas.dto.SaleDTO;
import com.breno_barbosa1.sistema_vendas.exception.SaleNotFoundException;
import com.breno_barbosa1.sistema_vendas.service.SaleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static com.breno_barbosa1.sistema_vendas.common.SaleConstants.getValidCreateSaleDTO;
import static com.breno_barbosa1.sistema_vendas.common.SaleConstants.getValidSaleDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(SaleController.class)
public class SaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SaleService saleService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void getSale_withExistingId_ReturnsOk() throws Exception {
        when(saleService.findById(1L)).thenReturn(getValidSaleDTO());

        mockMvc.perform(get("/api/sales/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(getValidSaleDTO().getId()))
            .andExpect(jsonPath("$.employeeId").value(getValidSaleDTO().getEmployeeId()))
            .andExpect(jsonPath("$.totalAmount").value(getValidSaleDTO().getTotalAmount()))
            .andExpect(jsonPath("$.saleItems").value(getValidSaleDTO().getSaleItems()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void getSale_withNonExistingId_ReturnsNotFound() throws Exception {
        when(saleService.findById(1L)).thenThrow(SaleNotFoundException.class);

        mockMvc.perform(get("/api/sales/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void createSale_withValidData_ReturnsCreated() throws Exception {
        when(saleService.create(any(CreateSaleDTO.class))).thenReturn(getValidSaleDTO());

        mockMvc.perform(post("/api/sales")
                .with(csrf())
                .content(objectMapper.writeValueAsString(getValidCreateSaleDTO()))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(getValidSaleDTO().getId()))
            .andExpect(jsonPath("$.employeeId").value(getValidSaleDTO().getEmployeeId()))
            .andExpect(jsonPath("$.totalAmount").value(getValidSaleDTO().getTotalAmount()))
            .andExpect(jsonPath("$.saleItems").value(getValidSaleDTO().getSaleItems()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void createSale_withInvalidData_ReturnsUnprocessableEntity() throws Exception {
        SaleDTO saleDTO = getValidSaleDTO();
        saleDTO.setEmployeeId(null);

        mockMvc.perform(post("/api/sales")
                .with(csrf())
                .content(objectMapper.writeValueAsString(getValidSaleDTO()))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableContent())
            .andExpect(jsonPath("$.message").value("Please verify if all fields are set correctly!"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteSale_withValidId_ReturnsNoContent() throws Exception {
        doNothing().when(saleService).delete(1L);

        mockMvc.perform(delete("/api/sales/1")
                .with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteSale_withNonExistingId_ReturnsNotFound() throws Exception {
        doThrow(new SaleNotFoundException())
            .when(saleService)
            .delete(99L);

        mockMvc.perform(delete("/api/sales/99")
                .with(csrf()))
            .andExpect(status().isNotFound());
    }
}