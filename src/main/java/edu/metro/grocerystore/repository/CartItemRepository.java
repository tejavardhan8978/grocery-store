package edu.metro.grocerystore.repository;

import edu.metro.grocerystore.model.Cart;
import edu.metro.grocerystore.model.CartItem;
import edu.metro.grocerystore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    
    /**
     * Find all items in a specific cart
     */
    List<CartItem> findByCart(Cart cart);
    
    /**
     * Find a specific cart item by cart and product
     */
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    
    /**
     * Delete all items in a cart
     */
    void deleteByCart(Cart cart);
}
