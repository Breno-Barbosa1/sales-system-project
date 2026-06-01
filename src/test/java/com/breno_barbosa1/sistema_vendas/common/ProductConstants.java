package com.breno_barbosa1.sistema_vendas.common;

import com.breno_barbosa1.sistema_vendas.dto.ProductDTO;
import com.breno_barbosa1.sistema_vendas.entity.Product;

import java.math.BigDecimal;

public class ProductConstants {

    public static Product getValidProduct() {
        return new Product("Dell Laptop", BigDecimal.valueOf(1000), BigDecimal.valueOf(700), 10);
    }

    public static Product getValidUpdateProduct() {
        return new Product("Dell Laptop", BigDecimal.valueOf(1000), BigDecimal.valueOf(700), 10);
    }

    public static ProductDTO getValidProductDTO() {
        return new ProductDTO("Dell Laptop", BigDecimal.valueOf(1200), BigDecimal.valueOf(700), 10);
    }

    public static ProductDTO getValidUpdateProductDTO() {
        return new ProductDTO("Dell Laptop", BigDecimal.valueOf(1200), BigDecimal.valueOf(700), 10);
    }
}
