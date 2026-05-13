package com.dongato.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Cafetería Don Gato - Inventory Management System.
 * <p>
 * Entry point for the Spring Boot application.
 * Uses Clean Architecture with McCall quality model traceability.
 */
@SpringBootApplication
public class DonGatoInventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DonGatoInventoryApplication.class, args);
    }
}
