package com.breno_barbosa1.sistema_vendas.controller;

import com.breno_barbosa1.sistema_vendas.dto.ProductDTO;
import com.breno_barbosa1.sistema_vendas.exception.ProductAlreadyRegisteredException;
import com.breno_barbosa1.sistema_vendas.exception.ProductNotFoundException;
import com.breno_barbosa1.sistema_vendas.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static com.breno_barbosa1.sistema_vendas.common.ProductConstants.getValidProductDTO;
import static com.breno_barbosa1.sistema_vendas.common.ProductConstants.getValidUpdateProductDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void getProduct_withExistingId_ReturnsOk() throws Exception {
         when(productService.findById(1L)).thenReturn(getValidProductDTO());

         mockMvc.perform(get("/api/products/1"))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.productName").value(getValidProductDTO().getProductName()))
             .andExpect(jsonPath("$.sellingPrice").value(getValidProductDTO().getSellingPrice()))
             .andExpect(jsonPath("$.priceAtPurchase").value(getValidProductDTO().getPriceAtPurchase()))
             .andExpect(jsonPath("$.stockQuantity").value(getValidProductDTO().getStockQuantity()));
     }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void getProduct_withNonExistingId_ReturnsNotFound() throws Exception {
         when(productService.findById(1L)).thenThrow(ProductNotFoundException.class);

         mockMvc.perform(get("/api/products/1"))
             .andExpect(status().isNotFound());
     }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void getProduct_withExistingName_ReturnsOk() throws Exception {
        when(productService.findByProductName("Dell Laptop")).thenReturn(getValidProductDTO());

        mockMvc.perform(get("/api/products/name/Dell Laptop"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productName").value(getValidProductDTO().getProductName()))
            .andExpect(jsonPath("$.sellingPrice").value(getValidProductDTO().getSellingPrice()))
            .andExpect(jsonPath("$.priceAtPurchase").value(getValidProductDTO().getPriceAtPurchase()))
            .andExpect(jsonPath("$.stockQuantity").value(getValidProductDTO().getStockQuantity()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void getProduct_withNonExistingName_ReturnsNotFound() throws Exception {
        when(productService.findByProductName(any())).thenThrow(ProductNotFoundException.class);

        mockMvc.perform(get("/api/products/name/Dell Laptop"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void createProduct_withValidData_ReturnsOk() throws Exception {
        when(productService.create(any(ProductDTO.class))).thenReturn(getValidProductDTO());

        mockMvc.perform(post("/api/products")
                .with(csrf())
                .content(objectMapper.writeValueAsString(getValidProductDTO()))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.productName").value(getValidProductDTO().getProductName()))
            .andExpect(jsonPath("$.sellingPrice").value(getValidProductDTO().getSellingPrice()))
            .andExpect(jsonPath("$.priceAtPurchase").value(getValidProductDTO().getPriceAtPurchase()))
            .andExpect(jsonPath("$.stockQuantity").value(getValidProductDTO().getStockQuantity()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void createProduct_withInvalidData_ReturnsUnprocessableEntity() throws Exception {
        ProductDTO productDTO = getValidProductDTO();
        productDTO.setProductName("");

        mockMvc.perform(post("/api/products")
                .with(csrf())
                .content(objectMapper.writeValueAsString(productDTO))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableContent())
            .andExpect(jsonPath("$.message").value("Please verify if all fields are set correctly!"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void createEmployee_thatAlreadyExists_ReturnsConflict() throws Exception {
        when(productService.create(any(ProductDTO.class))).thenThrow(ProductAlreadyRegisteredException.class);

        mockMvc.perform(post("/api/products")
                .with(csrf())
                .content(objectMapper.writeValueAsString(getValidProductDTO())).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void updateProduct_withValidData_ReturnsOk() throws Exception {
        ProductDTO productDTO = getValidUpdateProductDTO();
        productDTO.setProductName("Lenovo Laptop");
        ProductDTO updatedProduct = getValidProductDTO();
        updatedProduct.setProductName(productDTO.getProductName());

        when(productService.update(any(ProductDTO.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products")
                .with(csrf())
                .content(objectMapper.writeValueAsString(updatedProduct)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productName").value(updatedProduct.getProductName()))
            .andExpect(jsonPath("$.sellingPrice").value(updatedProduct.getSellingPrice()))
            .andExpect(jsonPath("$.priceAtPurchase").value(updatedProduct.getPriceAtPurchase()))
            .andExpect(jsonPath("$.stockQuantity").value(updatedProduct.getStockQuantity()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void updateProduct_withInvalidData_ReturnsUnprocessableEntity() throws Exception {
        ProductDTO productDTO = getValidProductDTO();
        productDTO.setProductName("");

        mockMvc.perform(put("/api/products")
                .with(csrf())
                .content(objectMapper.writeValueAsString(productDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableContent())
            .andExpect(jsonPath("$.message").value("Please verify if all fields are set correctly!"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteProduct_withValidId_ReturnsNoContent() throws Exception {
        doNothing().when(productService).delete(1L);

        mockMvc.perform(delete("/api/products/1")
                .with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteProduct_withNonExistingId_ReturnsNotFound() throws Exception {
        doThrow(new ProductNotFoundException())
            .when(productService)
            .delete(99L);

        mockMvc.perform(delete("/api/products/99")
                .with(csrf()))
            .andExpect(status().isNotFound());
    }
}