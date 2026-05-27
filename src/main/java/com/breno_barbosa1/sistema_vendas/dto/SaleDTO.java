package com.breno_barbosa1.sistema_vendas.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SaleDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Double totalAmount;

    private Long employee_id;

    private List<SaleItemDTO> saleItemDTOS;

    public SaleDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Long employee_id) {
        this.employee_id = employee_id;
    }

    public List<SaleItemDTO> getSaleItemDTOS() {
        return saleItemDTOS;
    }

    public void setSaleItemDTOS(List<SaleItemDTO> saleItemDTOS) {
        this.saleItemDTOS = saleItemDTOS;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SaleDTO saleDTO = (SaleDTO) o;
        return Objects.equals(getId(), saleDTO.getId()) && Objects.equals(getTotalAmount(), saleDTO.getTotalAmount()) && Objects.equals(getEmployee_id(), saleDTO.getEmployee_id()) && Objects.equals(getSaleItemDTOS(), saleDTO.getSaleItemDTOS());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTotalAmount(), getEmployee_id(), getSaleItemDTOS());
    }
}