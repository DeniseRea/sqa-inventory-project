package com.dongato.inventory.infrastructure.persistence.adapter;

import com.dongato.inventory.application.ports.out.ProductRepositoryPort;
import com.dongato.inventory.domain.model.Product;
import com.dongato.inventory.domain.model.ProductStatus;
import com.dongato.inventory.infrastructure.persistence.mapper.ProductPersistenceMapper;
import com.dongato.inventory.infrastructure.persistence.repository.JpaProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter that implements ProductRepositoryPort using Spring Data JPA.
 * <p>
 * McCall Factor: Portability — database-agnostic through JPA abstraction.
 */
@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final JpaProductRepository jpaRepository;

    @Override
    public Product save(Product product) {
        var entity = ProductPersistenceMapper.toEntity(product);
        var saved = jpaRepository.save(entity);
        return ProductPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id)
                .map(ProductPersistenceMapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream()
                .map(ProductPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        return jpaRepository.findByCategoryId(categoryId).stream()
                .map(ProductPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByStatus(ProductStatus status) {
        return jpaRepository.findByStatus(status).stream()
                .map(ProductPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByNameContainingIgnoreCase(String name) {
        return jpaRepository.findByNameContainingIgnoreCase(name).stream()
                .map(ProductPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
