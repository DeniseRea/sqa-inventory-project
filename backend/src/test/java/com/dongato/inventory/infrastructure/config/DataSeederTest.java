package com.dongato.inventory.infrastructure.config;

import com.dongato.inventory.infrastructure.persistence.entity.CategoryEntity;
import com.dongato.inventory.infrastructure.persistence.entity.ProductEntity;
import com.dongato.inventory.infrastructure.persistence.repository.JpaCategoryRepository;
import com.dongato.inventory.infrastructure.persistence.repository.JpaProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataSeederTest {

    private static final int EXPECTED_CATEGORY_COUNT = 3;
    private static final int EXPECTED_PRODUCT_COUNT = 11;

    private final DataSeeder dataSeeder = new DataSeeder();

    @Test
    @DisplayName("Should skip seeding when database already has categories")
    void shouldSkipSeedingWhenAlreadySeeded() throws Exception {
        JpaCategoryRepository categoryRepo = mock(JpaCategoryRepository.class);
        JpaProductRepository productRepo = mock(JpaProductRepository.class);
        when(categoryRepo.count()).thenReturn(1L);

        CommandLineRunner runner = dataSeeder.seedData(categoryRepo, productRepo);
        runner.run();

        verify(categoryRepo, never()).save(any(CategoryEntity.class));
        verifyNoInteractions(productRepo);
    }

    @Test
    @DisplayName("Should seed categories and products when database is empty")
    void shouldSeedWhenEmpty() throws Exception {
        JpaCategoryRepository categoryRepo = mock(JpaCategoryRepository.class);
        JpaProductRepository productRepo = mock(JpaProductRepository.class);
        when(categoryRepo.count()).thenReturn(0L);
        AtomicLong idGenerator = new AtomicLong(1);
        when(categoryRepo.save(any(CategoryEntity.class))).thenAnswer(invocation -> {
            CategoryEntity entity = invocation.getArgument(0);
            entity.setId(idGenerator.getAndIncrement());
            return entity;
        });
        when(productRepo.save(any(ProductEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CommandLineRunner runner = dataSeeder.seedData(categoryRepo, productRepo);
        runner.run();

        verify(categoryRepo, times(EXPECTED_CATEGORY_COUNT)).save(any(CategoryEntity.class));
        verify(productRepo, times(EXPECTED_PRODUCT_COUNT)).save(any(ProductEntity.class));

        ArgumentCaptor<ProductEntity> productCaptor = ArgumentCaptor.forClass(ProductEntity.class);
        verify(productRepo, atLeastOnce()).save(productCaptor.capture());
        ProductEntity firstProduct = productCaptor.getAllValues().get(0);
        assertEquals(1L, firstProduct.getCategoryId());
    }
}
