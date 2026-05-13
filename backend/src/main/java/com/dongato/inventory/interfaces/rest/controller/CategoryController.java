package com.dongato.inventory.interfaces.rest.controller;

import com.dongato.inventory.application.usecases.CategoryUseCase;
import com.dongato.inventory.domain.model.Category;
import com.dongato.inventory.interfaces.rest.dto.CategoryCreateDTO;
import com.dongato.inventory.interfaces.rest.dto.CategoryResponseDTO;
import com.dongato.inventory.interfaces.rest.mapper.CategoryRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Category management.
 * <p>
 * McCall Factors: Interoperability (REST standard), Integrity (input validation).
 */
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryUseCase categoryUseCase;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@Valid @RequestBody CategoryCreateDTO dto) {
        Category category = CategoryRestMapper.toDomain(dto);
        Category created = categoryUseCase.create(category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CategoryRestMapper.toResponseDto(created));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAll() {
        List<CategoryResponseDTO> categories = categoryUseCase.findAll().stream()
                .map(CategoryRestMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable Long id) {
        Category category = categoryUseCase.findById(id);
        return ResponseEntity.ok(CategoryRestMapper.toResponseDto(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryCreateDTO dto) {
        Category category = CategoryRestMapper.toDomain(dto);
        Category updated = categoryUseCase.update(id, category);
        return ResponseEntity.ok(CategoryRestMapper.toResponseDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
