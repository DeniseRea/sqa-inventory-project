package com.dongato.inventory.infrastructure.config;

import com.dongato.inventory.infrastructure.persistence.entity.CategoryEntity;
import com.dongato.inventory.infrastructure.persistence.entity.ProductEntity;
import com.dongato.inventory.infrastructure.persistence.repository.JpaCategoryRepository;
import com.dongato.inventory.infrastructure.persistence.repository.JpaProductRepository;
import com.dongato.inventory.domain.model.ProductStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Seeds the database with initial cafeteria data on first run.
 * Only inserts if the database is empty.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    @Bean
    public CommandLineRunner seedData(
            JpaCategoryRepository categoryRepo,
            JpaProductRepository productRepo) {
        return args -> {
            if (categoryRepo.count() > 0) {
                log.info("Database already seeded — skipping");
                return;
            }

            log.info("Seeding initial data for Cafetería Don Gato...");
            LocalDateTime now = LocalDateTime.now();

            // Categories
            CategoryEntity cafe = categoryRepo.save(CategoryEntity.builder()
                    .name("CAFE").description("Bebidas de café preparadas").createdAt(now).build());
            CategoryEntity bebida = categoryRepo.save(CategoryEntity.builder()
                    .name("BEBIDA").description("Bebidas frías y calientes no-café").createdAt(now).build());
            CategoryEntity snack = categoryRepo.save(CategoryEntity.builder()
                    .name("SNACK").description("Bocadillos y acompañamientos").createdAt(now).build());

            // Products — CAFE
            productRepo.save(ProductEntity.builder()
                    .name("Americano").price(new BigDecimal("2.50")).stock(50)
                    .status(ProductStatus.AVAILABLE).categoryId(cafe.getId()).createdAt(now).build());
            productRepo.save(ProductEntity.builder()
                    .name("Cappuccino").price(new BigDecimal("3.50")).stock(40)
                    .status(ProductStatus.AVAILABLE).categoryId(cafe.getId()).createdAt(now).build());
            productRepo.save(ProductEntity.builder()
                    .name("Latte").price(new BigDecimal("3.75")).stock(35)
                    .status(ProductStatus.AVAILABLE).categoryId(cafe.getId()).createdAt(now).build());
            productRepo.save(ProductEntity.builder()
                    .name("Espresso").price(new BigDecimal("2.00")).stock(60)
                    .status(ProductStatus.AVAILABLE).categoryId(cafe.getId()).createdAt(now).build());
            productRepo.save(ProductEntity.builder()
                    .name("Mocha").price(new BigDecimal("4.00")).stock(30)
                    .status(ProductStatus.AVAILABLE).categoryId(cafe.getId()).createdAt(now).build());

            // Products — BEBIDA
            productRepo.save(ProductEntity.builder()
                    .name("Jugo de Naranja").price(new BigDecimal("2.50")).stock(25)
                    .status(ProductStatus.AVAILABLE).categoryId(bebida.getId()).createdAt(now).build());
            productRepo.save(ProductEntity.builder()
                    .name("Té Verde").price(new BigDecimal("2.00")).stock(40)
                    .status(ProductStatus.AVAILABLE).categoryId(bebida.getId()).createdAt(now).build());
            productRepo.save(ProductEntity.builder()
                    .name("Agua Mineral").price(new BigDecimal("1.50")).stock(100)
                    .status(ProductStatus.AVAILABLE).categoryId(bebida.getId()).createdAt(now).build());

            // Products — SNACK
            productRepo.save(ProductEntity.builder()
                    .name("Croissant").price(new BigDecimal("2.75")).stock(20)
                    .status(ProductStatus.AVAILABLE).categoryId(snack.getId()).createdAt(now).build());
            productRepo.save(ProductEntity.builder()
                    .name("Muffin de Arándano").price(new BigDecimal("3.00")).stock(15)
                    .status(ProductStatus.AVAILABLE).categoryId(snack.getId()).createdAt(now).build());
            productRepo.save(ProductEntity.builder()
                    .name("Sándwich Club").price(new BigDecimal("5.50")).stock(10)
                    .status(ProductStatus.AVAILABLE).categoryId(snack.getId()).createdAt(now).build());

            log.info("Database seeded with {} categories and {} products",
                    categoryRepo.count(), productRepo.count());
        };
    }
}
