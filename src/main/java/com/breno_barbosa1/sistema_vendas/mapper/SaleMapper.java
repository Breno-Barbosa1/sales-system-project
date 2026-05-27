package com.breno_barbosa1.sistema_vendas.mapper;

import com.breno_barbosa1.sistema_vendas.dto.SaleDTO;
import com.breno_barbosa1.sistema_vendas.entity.Sale;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    Sale saleDTOtoSale(SaleDTO saleDTO);
    SaleDTO saleToSaleDTO(Sale sale);
}