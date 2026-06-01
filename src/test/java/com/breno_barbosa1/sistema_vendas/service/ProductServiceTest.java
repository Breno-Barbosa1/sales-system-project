package com.breno_barbosa1.sistema_vendas.service;

import com.breno_barbosa1.sistema_vendas.dto.ProductDTO;
import com.breno_barbosa1.sistema_vendas.entity.Product;
import com.breno_barbosa1.sistema_vendas.exception.ProductNotFoundException;
import com.breno_barbosa1.sistema_vendas.mapper.ProductMapper;
import com.breno_barbosa1.sistema_vendas.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.breno_barbosa1.sistema_vendas.common.ProductConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Test
    public void getProduct_withExistingId_ReturnsProduct() {
        Product product = getValidProduct();
        ProductDTO productDTO = getValidProductDTO();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.productToProductDTO(product)).thenReturn(productDTO);

        ProductDTO sut = productService.findById(1L);

        assertThat(sut).isNotNull();
        assertThat(sut)
            .usingRecursiveComparison()
            .isEqualTo(productDTO);

        verify(productRepository).findById(1L);
        verify(productMapper).productToProductDTO(product);
    }

    @Test
    public void getProduct_withNonExistingId_ThrowsException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(1L)).isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    public void getProduct_withExistingName_ReturnsProduct() {
        Product product = getValidProduct();
        ProductDTO productDTO = getValidProductDTO();

        when(productRepository.findByProductName("Dell Laptop")).thenReturn(Optional.of(product));
        when(productMapper.productToProductDTO(product)).thenReturn(productDTO);

        ProductDTO sut = productService.findByProductName("Dell Laptop");

        assertThat(sut).isNotNull();
        assertThat(sut)
            .usingRecursiveComparison()
            .isEqualTo(productDTO);

        verify(productRepository).findByProductName("Dell Laptop");
        verify(productMapper).productToProductDTO(product);
    }

    @Test
    public void getProduct_withNonExistingName_ThrowsException() {
        when(productRepository.findByProductName(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findByProductName(any())).isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    public void createProduct_withValidData_ReturnsProduct() {
        Product product = getValidProduct();
        ProductDTO productDTO = getValidProductDTO();

        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.productDTOToProduct(productDTO)).thenReturn(product);
        when(productMapper.productToProductDTO(product)).thenReturn(productDTO);

        ProductDTO sut = productService.create(productDTO);

        assertThat(sut)
            .usingRecursiveComparison()
            .isEqualTo(productDTO);

        verify(productRepository).save(product);
        verify(productMapper).productToProductDTO(product);
        verify(productMapper).productDTOToProduct(productDTO);
    }

    @Test
    public void updateProduct_withValidData_ReturnsProduct() {
        Product product = getValidProduct();
        ProductDTO productDTO = getValidProductDTO();
        Product updatedProduct = getValidUpdateProduct();
        ProductDTO updatedProductDTO = getValidUpdateProductDTO();

        product.setId(1L);
        productDTO.setId(1L);
        updatedProduct.setId(1L);
        updatedProductDTO.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.productToProductDTO(any(Product.class))).thenReturn(productDTO);
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductDTO sut = productService.update(updatedProductDTO);

        assertThat(sut).isNotNull();
        assertThat(sut)
            .usingRecursiveComparison()
            .isEqualTo(productDTO);
    }

    @Test
    public void deleteProduct_withExistingId_ReturnsEmpty() {
        Product product = getValidProduct();
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        productService.delete(1L);

        verify(productRepository).findById(1L);
        verify(productRepository).delete(product);
    }

    @Test
    public void deleteProduct_withNonExistingId_ThrowsException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.delete(1L)).isInstanceOf(ProductNotFoundException.class);
    }
}