package com.dongato.inventory.infrastructure.persistence.repository;

import com.dongato.inventory.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for Category.
 */
@Repository
public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {

    boolean existsByNameIgnoreCase(String name);
}
