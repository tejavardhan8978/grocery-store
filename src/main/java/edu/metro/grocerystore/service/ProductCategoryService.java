package edu.metro.grocerystore.service;

import edu.metro.grocerystore.model.ProductCategory;
import edu.metro.grocerystore.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService {
    
    private final ProductCategoryRepository productCategoryRepository;
    
    @Autowired
    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }
    
    /**
     * Get all product categories ordered by name
     * @return list of categories sorted alphabetically
     */
    public List<ProductCategory> getAllCategories() {
        return productCategoryRepository.findAllOrderByCategoryName();
    }
    
    /**
     * Find category by ID
     * @param id category ID
     * @return Optional containing the category if found
     */
    public Optional<ProductCategory> findById(Integer id) {
        return productCategoryRepository.findById(id);
    }
    
    /**
     * Find category by name
     * @param categoryName the category name
     * @return Optional containing the category if found
     */
    public Optional<ProductCategory> findByName(String categoryName) {
        return productCategoryRepository.findByCategoryName(categoryName);
    }
    
    /**
     * Create a new product category
     * @param categoryName the category name
     * @return the saved category
     * @throws IllegalArgumentException if category name already exists
     */
    public ProductCategory createCategory(String categoryName) {
        if (productCategoryRepository.existsByCategoryName(categoryName)) {
            throw new IllegalArgumentException("Category already exists: " + categoryName);
        }
        
        ProductCategory category = new ProductCategory(categoryName);
        return productCategoryRepository.save(category);
    }
    
    /**
     * Update an existing category
     * @param category the category to update
     * @return the updated category
     */
    public ProductCategory updateCategory(ProductCategory category) {
        return productCategoryRepository.save(category);
    }
    
    /**
     * Delete a category by ID
     * @param id the category ID to delete
     */
    public void deleteCategory(Integer id) {
        productCategoryRepository.deleteById(id);
    }
}