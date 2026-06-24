package com.dongato.inventory.infrastructure.persistence.adapter;

import com.dongato.inventory.application.ports.out.CategoryRepositoryPort;
import com.dongato.inventory.domain.model.Category;
import com.dongato.inventory.infrastructure.persistence.mapper.CategoryPersistenceMapper;
import com.dongato.inventory.infrastructure.persistence.repository.JpaCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter that implements CategoryRepositoryPort using Spring Data JPA.
 * <p>
 * McCall Factor: Portability — swappable persistence implementation.
 */
@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final JpaCategoryRepository jpaRepository;

    @Override
    public Category save(Category category) {
        var entity = CategoryPersistenceMapper.toEntity(category);
        var saved = jpaRepository.save(entity);
        return CategoryPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return jpaRepository.findById(id)
                .map(CategoryPersistenceMapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll().stream()
                .map(CategoryPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    public Category findByName(String name) {
        var entity = jpaRepository.findByNameIgnoreCase(name);
        if (entity == null) {
            return null;
        }
        return CategoryPersistenceMapper.toDomain(entity);
    }

    public List<Category> findAllOrderedByName() {
        return jpaRepository.findAllByOrderByNameAsc().stream()
                .map(CategoryPersistenceMapper::toDomain)
                .toList();
    }

    public long count() {
        return jpaRepository.count();
    }

    public Category saveAndFlush(Category category) {
        var entity = CategoryPersistenceMapper.toEntity(category);
        var saved = jpaRepository.saveAndFlush(entity);
        return CategoryPersistenceMapper.toDomain(saved);
    }

    public void delete(Category category) {
        var entity = CategoryPersistenceMapper.toEntity(category);
        jpaRepository.delete(entity);
    }
}
