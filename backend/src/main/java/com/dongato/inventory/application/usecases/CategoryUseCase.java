package com.dongato.inventory.application.usecases;

import com.dongato.inventory.application.ports.out.CategoryRepositoryPort;
import com.dongato.inventory.domain.exception.BusinessRuleException;
import com.dongato.inventory.domain.exception.ResourceNotFoundException;
import com.dongato.inventory.domain.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Use case for Category management.
 * <p>
 * McCall Factors: Correctness (business rules), Reusability (framework-agnostic logic).
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryUseCase {

    private final CategoryRepositoryPort categoryRepository;

    @Transactional
    public Category create(Category category) {
        category.validate();

        if (categoryRepository.existsByName(category.getName())) {
            throw new BusinessRuleException(
                    String.format("Category with name '%s' already exists", category.getName()));
        }

        category.setCreatedAt(LocalDateTime.now());
        log.info("Creating category: {}", category.getName());
        return categoryRepository.save(category);
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category update(Long id, Category updated) {
        Category existing = findById(id);
        updated.validate();

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());

        log.info("Updating category id={}: {}", id, existing.getName());
        return categoryRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", id);
        }
        log.info("Deleting category id={}", id);
        categoryRepository.deleteById(id);
    }
}
