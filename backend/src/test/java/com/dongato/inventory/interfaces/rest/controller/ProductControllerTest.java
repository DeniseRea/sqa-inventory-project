package com.dongato.inventory.interfaces.rest.controller;

import com.dongato.inventory.application.usecases.ProductUseCase;
import com.dongato.inventory.domain.model.Product;
import com.dongato.inventory.domain.model.ProductStatus;
import com.dongato.inventory.interfaces.rest.dto.ProductCreateDTO;
import com.dongato.inventory.interfaces.rest.dto.ProductResponseDTO;
import com.dongato.inventory.interfaces.rest.dto.ProductUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductUseCase productUseCase;

    @InjectMocks
    private ProductController productController;

    private Product mockProduct() {
        return Product.builder()
                .id(1L).name("Test Product").price(new BigDecimal("10.00"))
                .stock(5).status(ProductStatus.AVAILABLE).categoryId(1L).build();
    }

    @Test
    @DisplayName("Should create product and return 201")
    void shouldCreateProduct() {
        ProductCreateDTO dto = new ProductCreateDTO("Test Product", new BigDecimal("10.00"), 5, 1L);
        when(productUseCase.create(any(Product.class))).thenReturn(mockProduct());

        ResponseEntity<ProductResponseDTO> response = productController.create(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Product", response.getBody().getName());
    }

    @Test
    @DisplayName("Should find all products and return 200")
    void shouldFindAll() {
        when(productUseCase.findAll()).thenReturn(List.of(mockProduct()));

        ResponseEntity<List<ProductResponseDTO>> response = productController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("Should find product by id and return 200")
    void shouldFindById() {
        when(productUseCase.findById(1L)).thenReturn(mockProduct());

        ResponseEntity<ProductResponseDTO> response = productController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    @DisplayName("Should find products by category and return 200")
    void shouldFindByCategory() {
        when(productUseCase.findByCategoryId(1L)).thenReturn(List.of(mockProduct()));

        ResponseEntity<List<ProductResponseDTO>> response = productController.findByCategory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("Should find products by status and return 200")
    void shouldFindByStatus() {
        when(productUseCase.findByStatus(ProductStatus.AVAILABLE)).thenReturn(List.of(mockProduct()));

        ResponseEntity<List<ProductResponseDTO>> response = productController.findByStatus(ProductStatus.AVAILABLE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("Should search products by name and return 200")
    void shouldSearchByName() {
        when(productUseCase.searchByName("Test")).thenReturn(List.of(mockProduct()));

        ResponseEntity<List<ProductResponseDTO>> response = productController.searchByName("Test");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("Should update product and return 200")
    void shouldUpdateProduct() {
        ProductUpdateDTO dto = new ProductUpdateDTO("Updated Product", new BigDecimal("15.00"), 1L);
        Product updated = mockProduct();
        updated.setName("Updated Product");
        updated.setPrice(new BigDecimal("15.00"));
        
        when(productUseCase.update(eq(1L), any(Product.class))).thenReturn(updated);

        ResponseEntity<ProductResponseDTO> response = productController.update(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Product", response.getBody().getName());
    }

    @Test
    @DisplayName("Should delete product and return 204")
    void shouldDeleteProduct() {
        doNothing().when(productUseCase).delete(1L);

        ResponseEntity<Void> response = productController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productUseCase).delete(1L);
    }
}
