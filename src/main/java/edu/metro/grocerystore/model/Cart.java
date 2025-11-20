package edu.metro.grocerystore.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_id")
    private Integer cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", unique = true)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    @Column(name="created_at")
    private Instant createdAt;

    @Column(name="updated_at")
    private Instant updatedAt;


    public Cart(){
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Cart(User user) {
        this.user = user;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }


    // Getters and Setters
    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }


    // Business methods
    public BigDecimal getTotal() {
        return cartItems.stream()
                .map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getTotalItems() {
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public void addItem(CartItem item) {
        cartItems.add(item);
        item.setCart(this);
        this.updatedAt = Instant.now();
    }

    public void removeItem(CartItem item) {
        cartItems.remove(item);
        item.setCart(null);
        this.updatedAt = Instant.now();
    }

    public void clearCart() {
        cartItems.clear();
        this.updatedAt = Instant.now();
    }
}
