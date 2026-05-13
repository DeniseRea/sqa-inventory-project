package com.dongato.inventory.application.usecases;

import com.dongato.inventory.application.ports.out.CategoryRepositoryPort;
import com.dongato.inventory.application.ports.out.ProductRepositoryPort;
import com.dongato.inventory.domain.exception.ResourceNotFoundException;
import com.dongato.inventory.domain.model.Product;
import com.dongato.inventory.domain.model.ProductStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Use case for Product management.
 * <p>
 * McCall Factors: Correctness (validation), Reusability (port-based), Integrity (status tracking).
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductUseCase {

    private final ProductRepositoryPort productRepository;
    private final CategoryRepositoryPort categoryRepository;

    @Transactional
    public Product create(Product product) {
        product.validate();
        validateCategoryExists(product.getCategoryId());

        if (product.getStock() == null) {
            product.setStock(0);
        }
        product.recalculateStatus();
        product.setCreatedAt(LocalDateTime.now());

        log.info("Creating product: {} (category={})", product.getName(), product.getCategoryId());
        return productRepository.save(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByCategoryId(Long categoryId) {
        validateCategoryExists(categoryId);
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> findByStatus(ProductStatus status) {
        return productRepository.findByStatus(status);
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public Product update(Long id, Product updated) {
        Product existing = findById(id);
        updated.validate();

        if (updated.getCategoryId() != null) {
            validateCategoryExists(updated.getCategoryId());
            existing.setCategoryId(updated.getCategoryId());
        }

        existing.setName(updated.getName());
        existing.setPrice(updated.getPrice());
        // Stock is NOT updated directly here — use StockMovement for traceability
        existing.recalculateStatus();

        log.info("Updating product id={}: {}", id, existing.getName());
        return productRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", id);
        }
        log.info("Deleting product id={}", id);
        productRepository.deleteById(id);
    }

    private void validateCategoryExists(Long categoryId) {
        if (categoryId != null && !categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category", categoryId);
        }
    }
}
