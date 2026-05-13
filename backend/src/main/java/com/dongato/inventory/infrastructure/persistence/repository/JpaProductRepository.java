package com.dongato.inventory.infrastructure.persistence.repository;

import com.dongato.inventory.domain.model.ProductStatus;
import com.dongato.inventory.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Product.
 */
@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByCategoryId(Long categoryId);

    List<ProductEntity> findByStatus(ProductStatus status);

    List<ProductEntity> findByNameContainingIgnoreCase(String name);
}
