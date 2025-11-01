package edu.metro.grocerystore.service;

import edu.metro.grocerystore.model.Product;
import edu.metro.grocerystore.model.ProductCategory;
import edu.metro.grocerystore.model.User;
import edu.metro.grocerystore.repository.ProductRepository;
import edu.metro.grocerystore.repository.ProductCategoryRepository;
import edu.metro.grocerystore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final UserRepository userRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository, ProductCategoryRepository productCategoryRepository,
                          ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if database is empty
        if (userRepository.count() == 0) {
            // Create sample users
            createSampleUsers();
        }
        
        // Check if product categories are empty
        if (productCategoryRepository.count() == 0) {
            // Create sample categories
            createSampleCategories();
        }
        
        // Check if products are empty
        if (productRepository.count() == 0) {
            // Create sample products
            createSampleProducts();
        }
    }

    private void createSampleUsers() {
        // Create a regular user
        User regularUser = new User();
        regularUser.setFirstName("John");
        regularUser.setLastName("Doe");
        regularUser.setEmail("john.doe@example.com");
        regularUser.setPassword("password123");
        regularUser.setPhone("555-0123");
        regularUser.setGuest(false);
        regularUser.setEmployee(false);
        regularUser.setAdmin(false);

        // Create an admin user
        User adminUser = new User();
        adminUser.setFirstName("Admin");
        adminUser.setLastName("User");
        adminUser.setEmail("admin@grocerystore.com");
        adminUser.setPassword("admin123");
        adminUser.setPhone("555-0001");
        adminUser.setGuest(false);
        adminUser.setEmployee(false);
        adminUser.setAdmin(true);

        // Create an employee user
        User employeeUser = new User();
        employeeUser.setFirstName("Jane");
        employeeUser.setLastName("Smith");
        employeeUser.setEmail("jane.smith@grocerystore.com");
        employeeUser.setPassword("employee123");
        employeeUser.setPhone("555-0002");
        employeeUser.setGuest(false);
        employeeUser.setEmployee(true);
        employeeUser.setAdmin(false);

        // Save users to database
        userRepository.save(regularUser);
        userRepository.save(adminUser);
        userRepository.save(employeeUser);

        logger.info("Sample users created:");
        logger.info("Regular user: john.doe@example.com / password123");
        logger.info("Admin user: admin@grocerystore.com / admin123");
        logger.info("Employee user: jane.smith@grocerystore.com / employee123");
    }

    private void createSampleCategories() {
        // Create sample product categories with icons
        ProductCategory fruits = new ProductCategory("Fruits", "/images/categories/fruits.jpg", "apple");
        fruits.setCategoryDescription("Fresh fruits and seasonal produce");

        ProductCategory vegetables = new ProductCategory("Vegetables", "/images/categories/vegetables.jpg", "carrot");
        vegetables.setCategoryDescription("Fresh vegetables and greens");

        ProductCategory dairy = new ProductCategory("Dairy", "/images/categories/dairy.jpg", "cup");
        dairy.setCategoryDescription("Milk, cheese, yogurt and dairy products");

        ProductCategory meat = new ProductCategory("Meat & Seafood", "/images/categories/meat.jpg", "fish");
        meat.setCategoryDescription("Fresh meat, poultry and seafood");

        ProductCategory bakery = new ProductCategory("Bakery", "/images/categories/bakery.jpg", "bread-slice");
        bakery.setCategoryDescription("Fresh bread, pastries and baked goods");

        ProductCategory beverages = new ProductCategory("Beverages", "/images/categories/beverages.jpg", "cup-hot");
        beverages.setCategoryDescription("Drinks, juices and beverages");

        ProductCategory snacks = new ProductCategory("Snacks", "/images/categories/snacks.jpg", "cookie");
        snacks.setCategoryDescription("Chips, crackers and snack foods");

        ProductCategory frozenFoods = new ProductCategory("Frozen Foods", "/images/categories/frozen.jpg", "snow");
        frozenFoods.setCategoryDescription("Frozen meals and ice cream");

        ProductCategory household = new ProductCategory("Household Items", "/images/categories/household.jpg", "house");
        household.setCategoryDescription("Cleaning supplies and household essentials");

        ProductCategory personalCare = new ProductCategory("Personal Care", "/images/categories/personal-care.jpg", "heart-pulse");
        personalCare.setCategoryDescription("Health and beauty products");

        // Save categories to database
        productCategoryRepository.save(fruits);
        productCategoryRepository.save(vegetables);
        productCategoryRepository.save(dairy);
        productCategoryRepository.save(meat);
        productCategoryRepository.save(bakery);
        productCategoryRepository.save(beverages);
        productCategoryRepository.save(snacks);
        productCategoryRepository.save(frozenFoods);
        productCategoryRepository.save(household);
        productCategoryRepository.save(personalCare);

        logger.info("Sample product categories created with icons:");
        logger.info("- Fruits, Vegetables, Dairy, Meat & Seafood, Bakery");
        logger.info("- Beverages, Snacks, Frozen Foods, Household Items, Personal Care");
    }
    
    private void createSampleProducts() {
        // Get categories for product assignment
        ProductCategory fruits = productCategoryRepository.findByCategoryName("Fruits").orElse(null);
        ProductCategory vegetables = productCategoryRepository.findByCategoryName("Vegetables").orElse(null);
        ProductCategory dairy = productCategoryRepository.findByCategoryName("Dairy").orElse(null);
        ProductCategory meat = productCategoryRepository.findByCategoryName("Meat & Seafood").orElse(null);
        ProductCategory bakery = productCategoryRepository.findByCategoryName("Bakery").orElse(null);
        ProductCategory beverages = productCategoryRepository.findByCategoryName("Beverages").orElse(null);
        ProductCategory snacks = productCategoryRepository.findByCategoryName("Snacks").orElse(null);
        ProductCategory frozen = productCategoryRepository.findByCategoryName("Frozen Foods").orElse(null);
        ProductCategory household = productCategoryRepository.findByCategoryName("Household Items").orElse(null);
        ProductCategory personalCare = productCategoryRepository.findByCategoryName("Personal Care").orElse(null);
        
        // Create sample products for each category
        if (fruits != null) {
            createProduct("Organic Apples", "APPLE-001", new BigDecimal("3.99"), 100, 
                         "Fresh organic red apples", "/images/products/apples.jpg", fruits);
            createProduct("Bananas", "BANANA-001", new BigDecimal("2.49"), 150, 
                         "Fresh yellow bananas", "/images/products/bananas.jpg", fruits);
            createProduct("Strawberries", "STRAWB-001", new BigDecimal("4.99"), 75, 
                         "Sweet fresh strawberries", "/images/products/strawberries.jpg", fruits);
        }
        
        if (vegetables != null) {
            createProduct("Organic Carrots", "CARROT-001", new BigDecimal("2.99"), 80, 
                         "Fresh organic baby carrots", "/images/products/carrots.jpg", vegetables);
            createProduct("Spinach", "SPINACH-001", new BigDecimal("3.49"), 60, 
                         "Fresh baby spinach leaves", "/images/products/spinach.jpg", vegetables);
            createProduct("Bell Peppers", "PEPPER-001", new BigDecimal("4.49"), 90, 
                         "Colorful bell peppers", "/images/products/peppers.jpg", vegetables);
        }
        
        if (dairy != null) {
            createProduct("Organic Milk", "MILK-001", new BigDecimal("4.99"), 40, 
                         "Fresh organic whole milk", "/images/products/milk.jpg", dairy);
            createProduct("Greek Yogurt", "YOGURT-001", new BigDecimal("5.99"), 35, 
                         "Plain Greek yogurt", "/images/products/yogurt.jpg", dairy);
            createProduct("Cheddar Cheese", "CHEESE-001", new BigDecimal("6.99"), 50, 
                         "Sharp cheddar cheese block", "/images/products/cheese.jpg", dairy);
        }
        
        if (meat != null) {
            createProduct("Chicken Breast", "CHICKEN-001", new BigDecimal("8.99"), 25, 
                         "Fresh boneless chicken breast", "/images/products/chicken.jpg", meat);
            createProduct("Ground Beef", "BEEF-001", new BigDecimal("7.99"), 30, 
                         "Lean ground beef", "/images/products/beef.jpg", meat);
            createProduct("Salmon Fillet", "SALMON-001", new BigDecimal("12.99"), 20, 
                         "Fresh Atlantic salmon fillet", "/images/products/salmon.jpg", meat);
        }
        
        if (bakery != null) {
            createProduct("Whole Wheat Bread", "BREAD-001", new BigDecimal("3.49"), 45, 
                         "Fresh whole wheat bread loaf", "/images/products/bread.jpg", bakery);
            createProduct("Croissants", "CROISS-001", new BigDecimal("4.99"), 30, 
                         "Buttery French croissants", "/images/products/croissants.jpg", bakery);
            createProduct("Bagels", "BAGEL-001", new BigDecimal("3.99"), 40, 
                         "Fresh everything bagels", "/images/products/bagels.jpg", bakery);
        }
        
        if (beverages != null) {
            createProduct("Orange Juice", "OJ-001", new BigDecimal("4.49"), 60, 
                         "Fresh squeezed orange juice", "/images/products/orange-juice.jpg", beverages);
            createProduct("Coffee Beans", "COFFEE-001", new BigDecimal("12.99"), 35, 
                         "Premium coffee beans", "/images/products/coffee.jpg", beverages);
            createProduct("Sparkling Water", "WATER-001", new BigDecimal("1.99"), 100, 
                         "Natural sparkling water", "/images/products/sparkling-water.jpg", beverages);
        }
        
        if (snacks != null) {
            createProduct("Potato Chips", "CHIPS-001", new BigDecimal("2.99"), 70, 
                         "Crispy potato chips", "/images/products/chips.jpg", snacks);
            createProduct("Mixed Nuts", "NUTS-001", new BigDecimal("7.99"), 45, 
                         "Premium mixed nuts", "/images/products/nuts.jpg", snacks);
            createProduct("Chocolate Bar", "CHOC-001", new BigDecimal("3.49"), 55, 
                         "Dark chocolate bar", "/images/products/chocolate.jpg", snacks);
        }
        
        if (frozen != null) {
            createProduct("Frozen Pizza", "PIZZA-001", new BigDecimal("6.99"), 25, 
                         "Margherita frozen pizza", "/images/products/pizza.jpg", frozen);
            createProduct("Ice Cream", "ICE-001", new BigDecimal("5.99"), 30, 
                         "Vanilla ice cream", "/images/products/ice-cream.jpg", frozen);
            createProduct("Frozen Vegetables", "FVEG-001", new BigDecimal("3.99"), 40, 
                         "Mixed frozen vegetables", "/images/products/frozen-veg.jpg", frozen);
        }
        
        if (household != null) {
            createProduct("Dish Soap", "SOAP-001", new BigDecimal("3.99"), 50, 
                         "Liquid dish soap", "/images/products/dish-soap.jpg", household);
            createProduct("Paper Towels", "PAPER-001", new BigDecimal("8.99"), 35, 
                         "Absorbent paper towels", "/images/products/paper-towels.jpg", household);
            createProduct("Laundry Detergent", "DETERG-001", new BigDecimal("11.99"), 25, 
                         "Liquid laundry detergent", "/images/products/detergent.jpg", household);
        }
        
        if (personalCare != null) {
            createProduct("Shampoo", "SHAMP-001", new BigDecimal("7.99"), 40, 
                         "Moisturizing shampoo", "/images/products/shampoo.jpg", personalCare);
            createProduct("Toothpaste", "TOOTH-001", new BigDecimal("4.49"), 60, 
                         "Fluoride toothpaste", "/images/products/toothpaste.jpg", personalCare);
            createProduct("Body Lotion", "LOTION-001", new BigDecimal("6.99"), 35, 
                         "Moisturizing body lotion", "/images/products/lotion.jpg", personalCare);
        }
        
        logger.info("Sample products created for all categories");
        logger.info("Total products: {}", productRepository.count());
    }
    
    private void createProduct(String name, String sku, BigDecimal price, Integer quantity, 
                              String description, String imageUrl, ProductCategory category) {
        Product product = new Product();
        product.setName(name);
        product.setSku(sku);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setDescription(description);
        product.setImageUrl(imageUrl);
        product.setCategory(category);
        product.setIsActive(true);
        
        productRepository.save(product);
    }
}