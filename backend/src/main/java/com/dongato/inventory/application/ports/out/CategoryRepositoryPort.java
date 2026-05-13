package com.dongato.inventory.application.ports.out;

import com.dongato.inventory.domain.model.Category;

import java.util.List;
import java.util.Optional;

/**
 * Output port for Category persistence.
 * <p>
 * McCall Factor: Reusability — abstracts persistence from business logic (DIP).
 */
public interface CategoryRepositoryPort {

    Category save(Category category);

    Optional<Category> findById(Long id);

    List<Category> findAll();

    boolean existsById(Long id);

    boolean existsByName(String name);

    void deleteById(Long id);
}
