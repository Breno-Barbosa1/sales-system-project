package com.breno_barbosa1.sistema_vendas.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class SaleItemDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long quantity;
    private Double price;
    private Long sale_id;
    private Long product_id;

    public SaleItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getSale_id() {
        return sale_id;
    }

    public void setSale_id(Long sale_id) {
        this.sale_id = sale_id;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SaleItemDTO that = (SaleItemDTO) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getQuantity(), that.getQuantity()) && Objects.equals(getPrice(), that.getPrice()) && Objects.equals(getSale_id(), that.getSale_id()) && Objects.equals(getProduct_id(), that.getProduct_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getQuantity(), getPrice(), getSale_id(), getProduct_id());
    }
}