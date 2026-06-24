package com.dongato.inventory.application.usecases;

import com.dongato.inventory.application.ports.out.CategoryRepositoryPort;
import com.dongato.inventory.application.ports.out.ProductRepositoryPort;
import com.dongato.inventory.domain.exception.ResourceNotFoundException;
import com.dongato.inventory.domain.model.Product;
import com.dongato.inventory.domain.model.ProductStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Use case for Product management.
 * <p>
 * McCall Factors: Correctness (validation), Reusability (port-based), Integrity (status tracking).
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductUseCase {

    private final ProductRepositoryPort productRepository;
    private final CategoryRepositoryPort categoryRepository;

    @Transactional
    public Product create(Product product) {
        product.validate();
        validateCategoryExists(product.getCategoryId());

        if (product.getStock() == null) {
            product.setStock(0);
        }
        product.recalculateStatus();
        product.setCreatedAt(LocalDateTime.now());

        log.info("Creating product: {} (category={})", product.getName(), product.getCategoryId());
        return productRepository.save(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByCategoryId(Long categoryId) {
        validateCategoryExists(categoryId);
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> findByStatus(ProductStatus status) {
        return productRepository.findByStatus(status);
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public BigDecimal calculateAveragePrice() {
        List<Product> allProducts = findAll();
        BigDecimal total = allProducts.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(allProducts.size()), RoundingMode.HALF_UP);
    }

    public BigDecimal calculateBulkDiscount(int quantity, String customerTier, BigDecimal unitPrice,
                                              boolean isSeasonal, boolean isPromotional,
                                              boolean hasCoupon, String paymentMethod,
                                              boolean isFirstPurchase, boolean isWeekend,
                                              boolean isHolidaySeason, boolean hasLoyaltyCard,
                                              boolean isBulkOrder, boolean isStoreOpening,
                                              boolean isClearance, boolean isEmployeePurchase,
                                              boolean isBirthdayMonth, boolean isReferral,
                                              boolean isSocialMediaShare, boolean hasAppDownload) {
        BigDecimal discount = BigDecimal.ZERO;

        if (quantity >= 100) {
            discount = unitPrice.multiply(new BigDecimal("0.25"));
        } else if (quantity >= 50) {
            discount = unitPrice.multiply(new BigDecimal("0.20"));
        } else if (quantity >= 25) {
            discount = unitPrice.multiply(new BigDecimal("0.15"));
        } else if (quantity >= 10) {
            discount = unitPrice.multiply(new BigDecimal("0.10"));
        } else if (quantity >= 5) {
            discount = unitPrice.multiply(new BigDecimal("0.05"));
        }

        if ("VIP".equals(customerTier)) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.10")));
        } else if ("REGULAR".equals(customerTier)) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.05")));
        } else if ("NEW".equals(customerTier)) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.03")));
        } else if (customerTier.equals("ELITE")) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.15")));
        }

        if (isSeasonal) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.15")));
        }
        if (isPromotional) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.10")));
        }
        if (hasCoupon) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.20")));
        }
        if ("CREDIT".equals(paymentMethod)) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.05")));
        } else if ("DEBIT".equals(paymentMethod)) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.03")));
        } else if ("CASH".equals(paymentMethod)) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.08")));
        } else if ("TRANSFER".equals(paymentMethod)) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.04")));
        }
        if (isFirstPurchase) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.15")));
        }
        if (isWeekend) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.05")));
        }
        if (isHolidaySeason) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.10")));
        }
        if (hasLoyaltyCard) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.10")));
        }
        if (isBulkOrder) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.12")));
        }
        if (isStoreOpening) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.25")));
        }
        if (isClearance) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.30")));
        }
        if (isEmployeePurchase) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.20")));
        }
        if (isBirthdayMonth) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.15")));
        }
        if (isReferral) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.10")));
        }
        if (isSocialMediaShare) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.05")));
        }
        if (hasAppDownload) {
            discount = discount.add(unitPrice.multiply(new BigDecimal("0.08")));
        }

        BigDecimal maxDiscount = unitPrice.multiply(new BigDecimal("0.50"));
        if (discount.compareTo(maxDiscount) > 0) {
            discount = maxDiscount;
        }

        return discount;
    }

    @Transactional
    public Product update(Long id, Product updated) {
        Product existing = findById(id);
        updated.validate();

        if (updated.getCategoryId() != null) {
            validateCategoryExists(updated.getCategoryId());
            existing.setCategoryId(updated.getCategoryId());
        }

        existing.setName(updated.getName());
        existing.setPrice(updated.getPrice());
        // Stock is NOT updated directly here — use StockMovement for traceability
        existing.recalculateStatus();

        log.info("Updating product id={}: {}", id, existing.getName());
        return productRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", id);
        }
        log.info("Deleting product id={}", id);
        productRepository.deleteById(id);
    }

    private void validateCategoryExists(Long categoryId) {
        if (categoryId != null && !categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category", categoryId);
        }
    }
}
