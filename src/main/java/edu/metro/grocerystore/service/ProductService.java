package edu.metro.grocerystore.service;

import edu.metro.grocerystore.DTO.ProductSearchCriteria;
import edu.metro.grocerystore.model.Product;
import edu.metro.grocerystore.model.ProductCategory;
import edu.metro.grocerystore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;
    
    @Autowired
    public ProductService(ProductRepository productRepository, ProductCategoryService productCategoryService) {
        this.productRepository = productRepository;
        this.productCategoryService = productCategoryService;
    }
    
    /**
     * Get all active products
     * @return list of active products
     */
    public List<Product> getAllActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }
    
    /**
     * Get all active products with pagination
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy field to sort by
     * @param sortDirection sort direction (ASC/DESC)
     * @return page of active products
     */
    public Page<Product> getAllActiveProducts(int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("DESC") ? 
                   Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findByIsActiveTrue(pageable);
    }
    
    /**
     * Get product by ID
     * @param id product ID
     * @return Optional containing the product if found
     */
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }
    
    /**
     * Get products by category
     * @param categoryId category ID
     * @return list of products in the category
     */
    public List<Product> getProductsByCategory(Integer categoryId) {
        return productRepository.findByCategoryIdAndIsActiveTrue(categoryId);
    }
    
    /**
     * Get products by category with pagination
     * @param categoryId category ID
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy field to sort by
     * @param sortDirection sort direction (ASC/DESC)
     * @return page of products in the category
     */
    public Page<Product> getProductsByCategory(Integer categoryId, int page, int size, String sortBy, String sortDirection) {
        Optional<ProductCategory> category = productCategoryService.findById(categoryId);
        if (category.isEmpty()) {
            return Page.empty();
        }
        
        Sort sort = sortDirection.equalsIgnoreCase("DESC") ? 
                   Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findByCategoryAndIsActiveTrue(category.get(), pageable);
    }
    
    /**
     * Search products by name
     * @param name name to search for
     * @return list of matching products
     */
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name);
    }
    
    /**
     * Get product by SKU
     * @param sku product SKU
     * @return Optional containing the product if found
     */
    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySkuAndIsActiveTrue(sku);
    }
    
    /**
     * Get products within price range
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of products within price range
     */
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceRangeAndIsActiveTrue(minPrice, maxPrice);
    }
    
    /**
     * Get featured products
     * @param limit maximum number of products to return
     * @return list of featured products
     */
    public List<Product> getFeaturedProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return productRepository.findFeaturedProducts(pageable);
    }
    
    /**
     * Search products with multiple criteria using DTO
     * @param criteria search criteria containing all parameters
     * @return page of matching products
     */
    public Page<Product> searchProducts(ProductSearchCriteria criteria) {
        Sort sort = criteria.getSortDirection().equalsIgnoreCase("DESC") ? 
                   Sort.by(criteria.getSortBy()).descending() : Sort.by(criteria.getSortBy()).ascending();
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);
        
        return productRepository.searchProducts(criteria.getSearchTerm(), criteria.getCategoryId(), 
                                               criteria.getMinPrice(), criteria.getMaxPrice(), pageable);
    }
    
    /**
     * Get low stock products
     * @return list of products with low stock
     */
    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }
    
    /**
     * Create a new product
     * @param product the product to create
     * @return the saved product
     * @throws IllegalArgumentException if SKU already exists
     */
    public Product createProduct(Product product) {
        if (productRepository.existsBySku(product.getSku())) {
            throw new IllegalArgumentException("Product with SKU " + product.getSku() + " already exists");
        }
        
        product.setIsActive(true);
        return productRepository.save(product);
    }
    
    /**
     * Update an existing product
     * @param product the product to update
     * @return the updated product
     */
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }
    
    /**
     * Deactivate a product (soft delete)
     * @param id product ID
     * @return true if product was deactivated, false if not found
     */
    public boolean deactivateProduct(Integer id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setIsActive(false);
            productRepository.save(product);
            return true;
        }
        return false;
    }
    
    /**
     * Delete a product permanently
     * @param id product ID
     */
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
    
    /**
     * Adjust product stock
     * @param id product ID
     * @param quantityChange quantity to add/subtract
     * @return true if stock was adjusted successfully
     */
    public boolean adjustStock(Integer id, Integer quantityChange) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if (product.adjustStock(quantityChange)) {
                productRepository.save(product);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Count products by category
     * @param categoryId category ID
     * @return number of active products in the category
     */
    public long countProductsByCategory(Integer categoryId) {
        Optional<ProductCategory> category = productCategoryService.findById(categoryId);
        if (category.isPresent()) {
            return productRepository.countByCategoryAndIsActiveTrue(category.get());
        }
        return 0;
    }
}