package com.breno_barbosa1.sistema_vendas.mapper;

import com.breno_barbosa1.sistema_vendas.dto.SaleItemDTO;
import com.breno_barbosa1.sistema_vendas.entity.SaleItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SaleItemMapper {

    SaleItem saleItemDTOToSaleItem(SaleItemDTO saleItemDTO);
    SaleItemDTO saleItemToSaleItemDTO(SaleItem saleItem);
}