package edu.metro.grocerystore.repository;

import edu.metro.grocerystore.model.Product;
import edu.metro.grocerystore.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    /**
     * Find all active products
     * @return list of active products
     */
    List<Product> findByIsActiveTrue();
    
    /**
     * Find all active products with pagination
     * @param pageable pagination information
     * @return page of active products
     */
    Page<Product> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find products by category
     * @param category the product category
     * @return list of products in the category
     */
    List<Product> findByCategoryAndIsActiveTrue(ProductCategory category);
    
    /**
     * Find products by category with pagination
     * @param category the product category
     * @param pageable pagination information
     * @return page of products in the category
     */
    Page<Product> findByCategoryAndIsActiveTrue(ProductCategory category, Pageable pageable);
    
    /**
     * Find products by category ID
     * @param categoryId the category ID
     * @return list of products in the category
     */
    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId AND p.isActive = true")
    List<Product> findByCategoryIdAndIsActiveTrue(@Param("categoryId") Integer categoryId);
    
    /**
     * Find products by name containing (case insensitive)
     * @param name the name to search for
     * @return list of matching products
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.isActive = true")
    List<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(@Param("name") String name);
    
    /**
     * Find products by SKU
     * @param sku the SKU to search for
     * @return optional product with the SKU
     */
    Optional<Product> findBySkuAndIsActiveTrue(String sku);
    
    /**
     * Find products within price range
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of products within price range
     */
    @Query("SELECT p FROM Product p WHERE p.price >= :minPrice AND p.price <= :maxPrice AND p.isActive = true ORDER BY p.price ASC")
    List<Product> findByPriceRangeAndIsActiveTrue(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    /**
     * Find products that are low in stock
     * @return list of products with quantity <= reorder level
     */
    @Query("SELECT p FROM Product p WHERE p.quantity <= p.reorderLevel AND p.isActive = true")
    List<Product> findLowStockProducts();
    
    /**
     * Find featured products (you can customize this logic)
     * @param limit maximum number of products to return
     * @return list of featured products
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.productId DESC")
    List<Product> findFeaturedProducts(Pageable pageable);
    
    /**
     * Search products by multiple criteria
     * @param searchTerm search term for name or description
     * @param categoryId category filter (optional)
     * @param minPrice minimum price filter (optional)
     * @param maxPrice maximum price filter (optional)
     * @param pageable pagination information
     * @return page of matching products
     */
    @Query("SELECT p FROM Product p WHERE " +
           "(:searchTerm IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "(:categoryId IS NULL OR p.category.categoryId = :categoryId) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "p.isActive = true")
    Page<Product> searchProducts(@Param("searchTerm") String searchTerm,
                                @Param("categoryId") Integer categoryId,
                                @Param("minPrice") BigDecimal minPrice,
                                @Param("maxPrice") BigDecimal maxPrice,
                                Pageable pageable);
    
    /**
     * Check if SKU exists
     * @param sku the SKU to check
     * @return true if SKU exists
     */
    boolean existsBySku(String sku);
    
    /**
     * Count products by category
     * @param category the product category
     * @return number of active products in the category
     */
    long countByCategoryAndIsActiveTrue(ProductCategory category);
    
    /**
     * Find all products (including inactive) - for admin/employee
     * @param pageable pagination information
     * @return page of all products
     */
    Page<Product> findAll(Pageable pageable);
    
    /**
     * Search products (including inactive) by multiple criteria - for admin/employee
     * @param searchTerm search term for name or description
     * @param categoryId category filter (optional)
     * @param minPrice minimum price filter (optional)
     * @param maxPrice maximum price filter (optional)
     * @param isActive active status filter (optional)
     * @param inStock stock availability filter (optional)
     * @param pageable pagination information
     * @return page of matching products
     */
    @Query("SELECT p FROM Product p WHERE " +
           "(:searchTerm IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "(:categoryId IS NULL OR p.category.categoryId = :categoryId) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "(:isActive IS NULL OR p.isActive = :isActive) AND " +
           "(:inStock IS NULL OR (:inStock = true AND p.quantity > 0) OR (:inStock = false AND p.quantity = 0))")
    Page<Product> searchAllProducts(@Param("searchTerm") String searchTerm,
                                    @Param("categoryId") Integer categoryId,
                                    @Param("minPrice") BigDecimal minPrice,
                                    @Param("maxPrice") BigDecimal maxPrice,
                                    @Param("isActive") Boolean isActive,
                                    @Param("inStock") Boolean inStock,
                                    Pageable pageable);
    
    /**
     * Get the maximum price of all products
     * @return maximum product price
     */
    @Query("SELECT MAX(p.price) FROM Product p")
    BigDecimal findMaxPrice();
}