package com.dongato.inventory.application.ports.out;

import com.dongato.inventory.domain.model.Product;
import com.dongato.inventory.domain.model.ProductStatus;

import java.util.List;
import java.util.Optional;

/**
 * Output port for Product persistence.
 * <p>
 * McCall Factor: Reusability — decouples domain from infrastructure.
 */
public interface ProductRepositoryPort {

    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByStatus(ProductStatus status);

    List<Product> findByNameContainingIgnoreCase(String name);

    boolean existsById(Long id);

    void deleteById(Long id);
}
