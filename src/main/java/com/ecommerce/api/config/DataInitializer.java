package com.ecommerce.api.config;

import com.ecommerce.api.entity.*;
import com.ecommerce.api.repository.CategoryRepository;
import com.ecommerce.api.repository.InventoryRepository;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public DataInitializer(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            InventoryRepository inventoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded. Skipping initial data load.");
            return;
        }

        log.info("Seeding initial e-commerce data...");

        // 1. Users
        User admin = new User(null, "admin", "admin@ecommerce.com", "admin123", "System Administrator", "+1-555-0100", Role.ROLE_ADMIN, "100 Admin Plaza, Tech City");
        User customer1 = new User(null, "johndoe", "john@example.com", "pass123", "John Doe", "+1-555-0199", Role.ROLE_CUSTOMER, "456 Market St, San Francisco, CA");
        User customer2 = new User(null, "janedoe", "jane@example.com", "pass123", "Jane Doe", "+1-555-0200", Role.ROLE_CUSTOMER, "789 Pine Ave, Seattle, WA");
        userRepository.save(admin);
        userRepository.save(customer1);
        userRepository.save(customer2);

        // 2. Categories
        Category electronics = categoryRepository.save(new Category(null, "Electronics", "Devices, gadgets, and tech accessories"));
        Category clothing = categoryRepository.save(new Category(null, "Clothing", "Apparel, footwear, and fashion accessories"));
        Category books = categoryRepository.save(new Category(null, "Books", "Physical and digital books across genres"));
        Category home = categoryRepository.save(new Category(null, "Home & Kitchen", "Appliances, cookware, and home essentials"));

        // 3. Products
        Product p1 = productRepository.save(new Product(null, "PROD-LAP-001", "MacBook Pro 16", "High performance M-series laptop with Liquid Retina display", new BigDecimal("2499.99"), electronics, "https://images.unsplash.com/photo-1517336714731-489689fd1ca8", true));
        Product p2 = productRepository.save(new Product(null, "PROD-AUD-002", "Noise-Cancelling Headphones", "Wireless over-ear headphones with active noise cancellation", new BigDecimal("299.99"), electronics, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e", true));
        Product p3 = productRepository.save(new Product(null, "PROD-CLO-003", "Organic Cotton Crew T-Shirt", "100% certified organic breathable cotton t-shirt", new BigDecimal("29.99"), clothing, "https://images.unsplash.com/photo-1521572267360-ee0c2909d518", true));
        Product p4 = productRepository.save(new Product(null, "PROD-BOK-004", "Effective Java (3rd Edition)", "Best practices and deep insights for professional Java development", new BigDecimal("45.00"), books, "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c", true));
        Product p5 = productRepository.save(new Product(null, "PROD-KIT-005", "Espresso Coffee Machine", "15-bar Italian pump espresso and cappuccino maker", new BigDecimal("189.50"), home, "https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6", true));

        // 4. Inventories (p3 has low stock to test low-stock alerts)
        inventoryRepository.save(new Inventory(null, p1, 50, 0, 10));
        inventoryRepository.save(new Inventory(null, p2, 120, 0, 15));
        inventoryRepository.save(new Inventory(null, p3, 4, 0, 10)); // Low stock (4 <= 10)
        inventoryRepository.save(new Inventory(null, p4, 85, 0, 20));
        inventoryRepository.save(new Inventory(null, p5, 30, 0, 8));

        log.info("Initial data successfully seeded.");
    }
}
