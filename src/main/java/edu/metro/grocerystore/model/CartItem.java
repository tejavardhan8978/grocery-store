package edu.metro.grocerystore.model;

import jakarta.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name="cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_item_id")
    private Integer cartItemId;

    @Column(name="cartItem_id")
    Integer cartItemId;

    @ManyToOne
    @JoinColumn(name="cart_id", insertable=false, updatable=false)
    private Cart cart;

    @Column(name="product_id")
    Integer productId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="product_id", nullable=false)
    private Product product;

    @Column(name="quantity", nullable=false)
    private Integer quantity;


    //Constructors
    public CartItem() {}

    public CartItem(Cart cart, Product product, Integer quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }


    //Getters/setters
    public Integer getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Integer cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    //Business methods
    public BigDecimal getSubTotal(){
        if (product != null && product.getPrice() != null && quantity != null) {
            return product.getPrice().multiply(new BigDecimal(quantity));
        }
        return BigDecimal.ZERO;
    }
}
