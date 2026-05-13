package com.dongato.inventory.interfaces.rest.controller;

import com.dongato.inventory.application.usecases.ProductUseCase;
import com.dongato.inventory.domain.model.Product;
import com.dongato.inventory.domain.model.ProductStatus;
import com.dongato.inventory.interfaces.rest.dto.ProductCreateDTO;
import com.dongato.inventory.interfaces.rest.dto.ProductResponseDTO;
import com.dongato.inventory.interfaces.rest.dto.ProductUpdateDTO;
import com.dongato.inventory.interfaces.rest.mapper.ProductRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Product management.
 * <p>
 * McCall Factors: Interoperability (REST standard), Correctness (validated input).
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductUseCase productUseCase;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductCreateDTO dto) {
        Product product = ProductRestMapper.toDomain(dto);
        Product created = productUseCase.create(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductRestMapper.toResponseDto(created));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        List<ProductResponseDTO> products = productUseCase.findAll().stream()
                .map(ProductRestMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        Product product = productUseCase.findById(id);
        return ResponseEntity.ok(ProductRestMapper.toResponseDto(product));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> findByCategory(@PathVariable Long categoryId) {
        List<ProductResponseDTO> products = productUseCase.findByCategoryId(categoryId).stream()
                .map(ProductRestMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProductResponseDTO>> findByStatus(@PathVariable ProductStatus status) {
        List<ProductResponseDTO> products = productUseCase.findByStatus(status).stream()
                .map(ProductRestMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchByName(@RequestParam String name) {
        List<ProductResponseDTO> products = productUseCase.searchByName(name).stream()
                .map(ProductRestMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateDTO dto) {
        Product product = ProductRestMapper.toDomain(dto);
        Product updated = productUseCase.update(id, product);
        return ResponseEntity.ok(ProductRestMapper.toResponseDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
