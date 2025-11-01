package edu.metro.grocerystore.repository;

import edu.metro.grocerystore.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    
    /**
     * Find category by name
     * @param categoryName the category name to search for
     * @return Optional containing the category if found
     */
    Optional<ProductCategory> findByCategoryName(String categoryName);
    
    /**
     * Find all categories ordered by name
     * @return list of categories sorted alphabetically
     */
    @Query("SELECT pc FROM ProductCategory pc ORDER BY pc.categoryName ASC")
    List<ProductCategory> findAllOrderByCategoryName();
    
    /**
     * Check if category exists by name
     * @param categoryName the category name to check
     * @return true if category exists, false otherwise
     */
    boolean existsByCategoryName(String categoryName);
}