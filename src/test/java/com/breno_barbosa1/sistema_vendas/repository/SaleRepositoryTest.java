package com.breno_barbosa1.sistema_vendas.repository;

import com.breno_barbosa1.sistema_vendas.common.EmployeeConstants;
import com.breno_barbosa1.sistema_vendas.entity.Employee;
import com.breno_barbosa1.sistema_vendas.entity.Sale;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Optional;

import static com.breno_barbosa1.sistema_vendas.common.SaleConstants.getValidSale;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class SaleRepositoryTest {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void getSale_withValidId_ReturnsSale() {
        Employee employee = EmployeeConstants.getValidEmployee();

        Employee savedEmployee = testEntityManager.persistFlushFind(employee);

        testEntityManager.clear();

        Sale sale = getValidSale();
        sale.setEmployee(savedEmployee);

        Sale savedSale = testEntityManager.persistFlushFind(sale);

        testEntityManager.clear();

        Optional<Sale> sut = saleRepository.findById(savedSale.getId());

        assertThat(sut).isPresent();
        assertThat(sut.get())
            .usingRecursiveComparison()
            .isEqualTo(savedSale);
    }

    @Test
    public void getSale_withNonExistingId_ReturnsEmpty() {
        Optional<Sale> sut = saleRepository.findById(1L);

        assertThat(sut).isEmpty();
    }

    @Test
    public void createSale_withValidData_ReturnsSale() {
        Employee employee = EmployeeConstants.getValidEmployee();

        Employee savedEmployee = testEntityManager.persistFlushFind(employee);

        testEntityManager.clear();

        Sale sale = getValidSale();
        sale.setEmployee(savedEmployee);

        Sale sut = saleRepository.save(sale);

        assertThat(sut).isNotNull();
        assertThat(sut)
            .usingRecursiveComparison()
            .isEqualTo(sale);
    }

    @Test
    public void createSale_withInvalidData_ThrowsException() {
        Sale sale = getValidSale();
        sale.setEmployee(null);

        assertThatThrownBy(() -> saleRepository.save(sale)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void deleteSale_withExistingId_ReturnsEmpty() {
        Employee employee = EmployeeConstants.getValidEmployee();

        Employee savedEmployee = testEntityManager.persistFlushFind(employee);

        testEntityManager.clear();

        Sale sale = getValidSale();
        sale.setEmployee(savedEmployee);

        Sale savedSale = testEntityManager.persistFlushFind(sale);

        testEntityManager.clear();

        Sale foundSale = testEntityManager.find(Sale.class, savedSale.getId());

        saleRepository.delete(foundSale);
        saleRepository.flush();

        Sale sut = testEntityManager.find(Sale.class, foundSale.getId());

        assertThat(sut).isNull();
    }

    @Test
    public void deleteSale_withNonExistingId_DoesNotThrowException() {
        Sale sale = new Sale();
        sale.setId(99L);

        assertThatCode(() -> {
            saleRepository.delete(sale);
            saleRepository.flush();
        }).doesNotThrowAnyException();
    }
}