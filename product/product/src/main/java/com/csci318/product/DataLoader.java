package com.csci318.product;

import com.csci318.product.model.entity.Product;
import com.csci318.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private final ProductRepository productRepository;

    public DataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create 30 sample products
        List<Product> products = Arrays.asList(
                Product.createProduct("Laptop", "High-performance laptop", "BrandA", 1500.00, 50, Arrays.asList("Electronics", "Computers")),
                Product.createProduct("Smartphone", "Latest model smartphone", "BrandB", 800.00, 100, Arrays.asList("Electronics", "Mobile")),
                Product.createProduct("Headphones", "Wireless noise-cancelling headphones", "BrandC", 200.00, 30, Arrays.asList("Audio", "Accessories")),
                Product.createProduct("Smartwatch", "Fitness tracking smartwatch", "BrandD", 300.00, 80, Arrays.asList("Wearables", "Fitness")),
                Product.createProduct("Tablet", "Lightweight tablet with stylus", "BrandE", 600.00, 25, Arrays.asList("Electronics", "Tablets")),
                Product.createProduct("Camera", "Digital SLR camera", "BrandF", 1200.00, 20, Arrays.asList("Photography", "Electronics")),
                Product.createProduct("Bluetooth Speaker", "Portable Bluetooth speaker", "BrandG", 150.00, 40, Arrays.asList("Audio", "Electronics")),
                Product.createProduct("Gaming Console", "Latest gaming console", "BrandH", 500.00, 60, Arrays.asList("Gaming", "Electronics")),
                Product.createProduct("External Hard Drive", "1TB portable external hard drive", "BrandI", 100.00, 90, Arrays.asList("Storage", "Computers")),
                Product.createProduct("Monitor", "27-inch 4K monitor", "BrandJ", 400.00, 35, Arrays.asList("Computers", "Accessories")),
                Product.createProduct("Keyboard", "Mechanical keyboard with RGB", "BrandK", 100.00, 70, Arrays.asList("Computers", "Accessories")),
                Product.createProduct("Mouse", "Wireless gaming mouse", "BrandL", 50.00, 85, Arrays.asList("Computers", "Accessories")),
                Product.createProduct("Chair", "Ergonomic office chair", "BrandM", 250.00, 15, Arrays.asList("Furniture", "Office")),
                Product.createProduct("Desk", "Adjustable standing desk", "BrandN", 400.00, 20, Arrays.asList("Furniture", "Office")),
                Product.createProduct("Router", "High-speed WiFi router", "BrandO", 180.00, 50, Arrays.asList("Networking", "Electronics")),
                Product.createProduct("Printer", "All-in-one wireless printer", "BrandP", 120.00, 30, Arrays.asList("Office", "Electronics")),
                Product.createProduct("Microwave", "Compact counter-top microwave", "BrandQ", 90.00, 60, Arrays.asList("Home Appliances", "Kitchen")),
                Product.createProduct("Vacuum Cleaner", "Robot vacuum cleaner", "BrandR", 350.00, 25, Arrays.asList("Home Appliances", "Cleaning")),
                Product.createProduct("Refrigerator", "Energy-efficient refrigerator", "BrandS", 1200.00, 10, Arrays.asList("Home Appliances", "Kitchen")),
                Product.createProduct("Washing Machine", "Front-load washing machine", "BrandT", 800.00, 15, Arrays.asList("Home Appliances", "Laundry")),
                Product.createProduct("Air Conditioner", "Portable air conditioner", "BrandU", 400.00, 20, Arrays.asList("Home Appliances", "Cooling")),
                Product.createProduct("Oven", "Convection oven with grill", "BrandV", 700.00, 18, Arrays.asList("Home Appliances", "Kitchen")),
                Product.createProduct("Television", "50-inch 4K smart TV", "BrandW", 1000.00, 35, Arrays.asList("Entertainment", "Electronics")),
                Product.createProduct("Dishwasher", "Energy-efficient dishwasher", "BrandX", 900.00, 12, Arrays.asList("Home Appliances", "Kitchen")),
                Product.createProduct("Coffee Maker", "Automatic coffee maker with grinder", "BrandY", 150.00, 40, Arrays.asList("Kitchen", "Appliances")),
                Product.createProduct("Blender", "High-speed blender", "BrandZ", 120.00, 50, Arrays.asList("Kitchen", "Appliances")),
                Product.createProduct("Water Bottle", "Insulated stainless steel water bottle", "BrandAA", 25.00, 100, Arrays.asList("Accessories", "Fitness")),
                Product.createProduct("Electric Toothbrush", "Rechargeable electric toothbrush", "BrandBB", 80.00, 60, Arrays.asList("Personal Care", "Health")),
                Product.createProduct("Yoga Mat", "Non-slip yoga mat", "BrandCC", 30.00, 80, Arrays.asList("Fitness", "Wellness")),
                Product.createProduct("Sunglasses", "Polarized sunglasses with UV protection", "BrandDD", 50.00, 70, Arrays.asList("Fashion", "Accessories"))
        );

        // Save all products in one go
        productRepository.saveAll(products);

        System.out.println("30 sample products loaded into the database.");
    }
}
