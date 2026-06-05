package com.breno_barbosa1.sistema_vendas.service;

import com.breno_barbosa1.sistema_vendas.dto.CreateSaleDTO;
import com.breno_barbosa1.sistema_vendas.dto.SaleDTO;
import com.breno_barbosa1.sistema_vendas.entity.Employee;
import com.breno_barbosa1.sistema_vendas.entity.Sale;
import com.breno_barbosa1.sistema_vendas.exception.SaleNotFoundException;
import com.breno_barbosa1.sistema_vendas.mapper.SaleMapper;
import com.breno_barbosa1.sistema_vendas.repository.EmployeeRepository;
import com.breno_barbosa1.sistema_vendas.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.breno_barbosa1.sistema_vendas.common.SaleConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaleServiceTest {

    @InjectMocks
    private SaleService saleService;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private SaleMapper saleMapper;

    @Test
    public void getSale_WithExistingId_ReturnsSale() {
        Sale sale = getValidSale();
        SaleDTO saleDTO = getValidSaleDTO();

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(saleMapper.saleToSaleDTO(sale)).thenReturn(saleDTO);

        SaleDTO sut = saleService.findById(1L);

        assertThat(sut).isNotNull();
        assertThat(sut)
            .usingRecursiveComparison()
            .isEqualTo(saleDTO);

        verify(saleRepository).findById(1L);
        verify(saleMapper).saleToSaleDTO(sale);
    }

    @Test
    public void getSale_WithNonExistingId_ThrowsException() {
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> saleService.findById(1L)).isInstanceOf(SaleNotFoundException.class);
    }

    @Test
    public void createSale_withValidData_ReturnsSale() {
        Sale sale = getValidSale();
        SaleDTO saleDTO = getValidSaleDTO();
        CreateSaleDTO createSaleDTO = getValidCreateSaleDTO();
        Employee employee = new Employee();

        when(saleRepository.saveAndFlush(any(Sale.class))).thenReturn(sale);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(saleMapper.saleToSaleDTO(sale)).thenReturn(saleDTO);
        when(saleMapper.createSaleDTOtoSale(createSaleDTO)).thenReturn(sale);

        SaleDTO sut = saleService.create(createSaleDTO);

        assertThat(sut).isNotNull();
        assertThat(sut)
            .usingRecursiveComparison()
            .isEqualTo(saleDTO);

        verify(saleRepository).saveAndFlush(sale);
        verify(saleMapper).saleToSaleDTO(sale);
    }

    @Test
    public void deleteSale_withExistingId_ReturnsEmpty() {
        Sale sale = getValidSale();
        sale.setId(1L);

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        doNothing().when(saleRepository).delete(sale);

        saleService.delete(1L);

        verify(saleRepository).findById(1L);
        verify(saleRepository).delete(sale);
    }

    @Test
    public void deleteSale_withNonExistingId_ThrowsException() {
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> saleService.delete(1L)).isInstanceOf(SaleNotFoundException.class);
    }
}