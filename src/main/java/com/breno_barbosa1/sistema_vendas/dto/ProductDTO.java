package com.breno_barbosa1.sistema_vendas.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class ProductDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long Id;
    private String productName;
    private Double sellingPrice;
    private Double priceAtPurchase;
    private Long stockQuantity;

    public ProductDTO() {}

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Double getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(Double priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    public Long getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Long stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO product = (ProductDTO) o;
        return Objects.equals(getId(), product.getId()) && Objects.equals(getProductName(), product.getProductName()) && Objects.equals(getSellingPrice(), product.getSellingPrice()) && Objects.equals(getPriceAtPurchase(), product.getPriceAtPurchase()) && Objects.equals(getStockQuantity(), product.getStockQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getProductName(), getSellingPrice(), getPriceAtPurchase(), getStockQuantity());
    }
}