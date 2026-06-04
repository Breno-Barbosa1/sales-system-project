package com.breno_barbosa1.sistema_vendas.repository;

import com.breno_barbosa1.sistema_vendas.entity.Product;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.math.BigDecimal;
import java.util.Optional;

import static com.breno_barbosa1.sistema_vendas.common.ProductConstants.getValidProduct;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void getProduct_withExistingId_ReturnsEmployee() {
        Product product = getValidProduct();

        Product savedProduct = testEntityManager.persistFlushFind(product);

        testEntityManager.clear();

        Optional<Product> sut = productRepository.findById(savedProduct.getId());

        assertThat(sut).isPresent();
        assertThat(sut.get())
            .usingRecursiveComparison()
            .isEqualTo(savedProduct);
    }

    @Test
    public void getProduct_withNonExistingId_ReturnsEmpty() {
        Optional<Product> sut = productRepository.findById(1L);

        assertThat(sut).isEmpty();
    }

    @Test
    public void getProduct_withExistingName_ReturnsProduct() {
        Product product = getValidProduct();

        Product savedProduct = testEntityManager.persistFlushFind(product);

        testEntityManager.clear();

        Optional<Product> sut = productRepository.findByProductName(savedProduct.getProductName());

        assertThat(sut).isPresent();
        assertThat(sut.get())
            .usingRecursiveComparison()
            .isEqualTo(savedProduct);
    }

    @Test
    public void getProduct_withNonExistingName_ReturnsEmpty() {
        Optional<Product> sut = productRepository.findByProductName("Dell Pc");

        assertThat(sut).isEmpty();
    }

    @Test
    public void createProduct_withValidData_ReturnsProduct() {
        Product product = getValidProduct();

        Product savedProduct = testEntityManager.persistAndFlush(product);

        testEntityManager.clear();

        Product sut = testEntityManager.find(Product.class, savedProduct.getId());

        assertThat(sut).isNotNull();
        assertThat(sut)
            .usingRecursiveComparison()
                .withComparatorForType(
                    BigDecimal::compareTo,
                    BigDecimal.class
                )
            .isEqualTo(savedProduct);
    }

    @Test
    public void createProduct_withInvalidData_ThrowsException() {
        Product product1 = getValidProduct();
        product1.setProductName("");
        Product product2 = getValidProduct();
        product2.setProductName("");

        assertThatThrownBy(() -> productRepository.saveAndFlush(product1)).isInstanceOf(ConstraintViolationException.class);
        assertThatThrownBy(() -> productRepository.saveAndFlush(product2)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void updateProduct_withValidData_ReturnsProduct() {
        Product product = getValidProduct();

        Product savedProduct = productRepository.saveAndFlush(product);

        testEntityManager.clear();

        Product foundProduct = testEntityManager.find(Product.class, savedProduct.getId());

        foundProduct.setProductName("Lenovo Laptop");

        productRepository.saveAndFlush(foundProduct);

        testEntityManager.clear();

        Product sut = testEntityManager.find(Product.class, savedProduct.getId());

        assertThat(sut).isNotNull();
        assertThat(sut)
            .usingRecursiveComparison()
            .isEqualTo(foundProduct);
    }

    @Test
    public void updateProduct_withInvalidData_ThrowsException() {
        Product product = getValidProduct();

        Product savedProduct = productRepository.saveAndFlush(product);

        testEntityManager.clear();

        Product foundProduct = testEntityManager.find(Product.class, savedProduct.getId());

        foundProduct.setProductName("");

        assertThatThrownBy(() -> productRepository.saveAndFlush(foundProduct)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void deleteProduct_withExistingId_ReturnsEmpty() {
        Product product = getValidProduct();

        Product savedProduct = productRepository.saveAndFlush(product);

        testEntityManager.clear();

        Product foundProduct = testEntityManager.find(Product.class, savedProduct.getId());

        productRepository.delete(foundProduct);
        productRepository.flush();

        testEntityManager.clear();

        Product sut = testEntityManager.find(Product.class, savedProduct.getId());

        assertThat(sut).isNull();
    }

    @Test
    public void deleteProduct_withNonExistingId_DoesNotThrowException() {
        Product product = new Product();
        product.setId(99L);

        assertThatCode(() -> {
            productRepository.delete(product);
            productRepository.flush();
        }).doesNotThrowAnyException();
    }
}