package com.breno_barbosa1.sistema_vendas.common;

import com.breno_barbosa1.sistema_vendas.dto.CreateSaleDTO;
import com.breno_barbosa1.sistema_vendas.dto.SaleDTO;
import com.breno_barbosa1.sistema_vendas.entity.Employee;
import com.breno_barbosa1.sistema_vendas.entity.Sale;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SaleConstants {

    public static Sale getValidSale() {
        Employee employee = EmployeeConstants.getValidEmployee();

        return new Sale(LocalDateTime.now(), BigDecimal.valueOf(2000),employee, new ArrayList<>());
    }

    public static SaleDTO getValidSaleDTO() {
        return new SaleDTO(new ArrayList<>(), 1L, BigDecimal.valueOf(2000));
    }

    public static CreateSaleDTO getValidCreateSaleDTO() {
        return new CreateSaleDTO(1L, new ArrayList<>());
    }
}