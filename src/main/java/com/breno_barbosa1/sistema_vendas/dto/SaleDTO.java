package com.breno_barbosa1.sistema_vendas.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class SaleDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private BigDecimal totalAmount;

    @NotNull
    private Long employeeId;

    @NotNull
    private List<SaleItemDTO> saleItems;

    public SaleDTO() {}

    public SaleDTO(List<SaleItemDTO> saleItems, Long employeeId, BigDecimal totalAmount) {
        this.saleItems = saleItems;
        this.employeeId = employeeId;
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<SaleItemDTO> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(List<SaleItemDTO> saleItems) {
        this.saleItems = saleItems;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SaleDTO saleDTO = (SaleDTO) o;
        return Objects.equals(getId(), saleDTO.getId()) && Objects.equals(getTotalAmount(), saleDTO.getTotalAmount()) && Objects.equals(getEmployeeId(), saleDTO.getEmployeeId()) && Objects.equals(getSaleItems(), saleDTO.getSaleItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTotalAmount(), getEmployeeId(), getSaleItems());
    }
}