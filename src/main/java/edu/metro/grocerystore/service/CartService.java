package edu.metro.grocerystore.service;

import edu.metro.grocerystore.model.Cart;
import edu.metro.grocerystore.model.CartItem;
import edu.metro.grocerystore.model.Product;
import edu.metro.grocerystore.model.User;
import edu.metro.grocerystore.repository.CartItemRepository;
import edu.metro.grocerystore.repository.CartRepository;
import edu.metro.grocerystore.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Service
public class CartService {
    
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    
    @Autowired
    public CartService(CartRepository cartRepository, 
                      CartItemRepository cartItemRepository,
                      ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }
    
    /**
     * Get or create cart for a user
     */
    public Cart getOrCreateCart(User user) {
        if (user == null || user.isGuest()) {
            throw new IllegalArgumentException("Cannot create cart for guest or null user");
        }
        
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });
    }
    
    /**
     * Get cart by user ID
     */
    public Optional<Cart> getCartByUserId(Integer userId) {
        return cartRepository.findByUser_Userid(userId);
    }
    
    /**
     * Add product to cart
     */
    @Transactional
    public Cart addProductToCart(User user, Integer productId, Integer quantity) {
        if (user == null || user.isGuest()) {
            throw new IllegalArgumentException("User must be logged in to add items to cart");
        }
        
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        
        // Get or create cart
        Cart cart = getOrCreateCart(user);
        
        // Get product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
        
        // Check if product is available
        if (!product.isAvailable()) {
            throw new IllegalArgumentException("Product is not available: " + product.getName());
        }
        
        // Check if sufficient stock
        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
        }
        
        // Check if product already in cart
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);
        
        if (existingItem.isPresent()) {
            // Update quantity
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;
            
            // Check stock for new quantity
            if (product.getQuantity() < newQuantity) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }
            
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        } else {
            // Add new item
            CartItem newItem = new CartItem(cart, product, quantity);
            cart.addItem(newItem);
            cartItemRepository.save(newItem);
        }
        
        cart.setUpdatedAt(Instant.now());
        return cartRepository.save(cart);
    }
    
    /**
     * Update cart item quantity
     */
    @Transactional
    public Cart updateCartItemQuantity(User user, Integer cartItemId, Integer quantity) {
        if (user == null || user.isGuest()) {
            throw new IllegalArgumentException("User must be logged in");
        }
        
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }
        
        Cart cart = getOrCreateCart(user);
        
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        
        // Verify item belongs to user's cart
        if (!item.getCart().getCartId().equals(cart.getCartId())) {
            throw new IllegalArgumentException("Cart item does not belong to user");
        }
        
        if (quantity == 0) {
            // Remove item
            cart.removeItem(item);
            cartItemRepository.delete(item);
        } else {
            // Check stock
            if (item.getProduct().getQuantity() < quantity) {
                throw new IllegalArgumentException("Insufficient stock for product: " + item.getProduct().getName());
            }
            
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        
        cart.setUpdatedAt(Instant.now());
        return cartRepository.save(cart);
    }
    
    /**
     * Remove item from cart
     */
    @Transactional
    public Cart removeItemFromCart(User user, Integer cartItemId) {
        if (user == null || user.isGuest()) {
            throw new IllegalArgumentException("User must be logged in");
        }
        
        Cart cart = getOrCreateCart(user);
        
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        
        // Verify item belongs to user's cart
        if (!item.getCart().getCartId().equals(cart.getCartId())) {
            throw new IllegalArgumentException("Cart item does not belong to user");
        }
        
        cart.removeItem(item);
        cartItemRepository.delete(item);
        
        cart.setUpdatedAt(Instant.now());
        return cartRepository.save(cart);
    }
    
    /**
     * Clear all items from cart
     */
    @Transactional
    public Cart clearCart(User user) {
        if (user == null || user.isGuest()) {
            throw new IllegalArgumentException("User must be logged in");
        }
        
        Cart cart = getOrCreateCart(user);
        cart.clearCart();
        cartItemRepository.deleteByCart(cart);
        
        cart.setUpdatedAt(Instant.now());
        return cartRepository.save(cart);
    }
    
    /**
     * Get cart item count for user
     */
    public int getCartItemCount(User user) {
        if (user == null || user.isGuest()) {
            return 0;
        }
        
        Optional<Cart> cartOpt = cartRepository.findByUser(user);
        return cartOpt.map(Cart::getTotalItems).orElse(0);
    }
    
    /**
     * Get cart total for user
     */
    public BigDecimal getCartTotal(User user) {
        if (user == null || user.isGuest()) {
            return BigDecimal.ZERO;
        }
        
        Optional<Cart> cartOpt = cartRepository.findByUser(user);
        return cartOpt.map(Cart::getTotal).orElse(BigDecimal.ZERO);
    }
}
