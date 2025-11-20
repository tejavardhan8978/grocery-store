package edu.metro.grocerystore.repository;

import edu.metro.grocerystore.model.Cart;
import edu.metro.grocerystore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    
    /**
     * Find cart by user
     */
    Optional<Cart> findByUser(User user);
    
    /**
     * Find cart by user ID
     */
    Optional<Cart> findByUser_Userid(Integer userId);
    
    /**
     * Check if cart exists for user
     */
    boolean existsByUser(User user);
}
