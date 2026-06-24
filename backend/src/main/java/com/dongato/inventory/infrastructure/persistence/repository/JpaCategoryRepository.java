package com.dongato.inventory.infrastructure.persistence.repository;

import com.dongato.inventory.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Category.
 */
@Repository
public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {

    boolean existsByNameIgnoreCase(String name);

    CategoryEntity findByNameIgnoreCase(String name);

    List<CategoryEntity> findAllByOrderByNameAsc();
}
