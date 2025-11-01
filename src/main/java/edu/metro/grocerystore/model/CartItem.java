package edu.metro.grocerystore.model;

import jakarta.persistence.*;


@Entity
public class CartItem {

    @Id
    @Column(name="cartitem_id")
    Integer cartItemId;

    @ManyToOne
    @JoinColumn(name = "cart_id", insertable = false, updatable = false)
    private Cart cart_id;

    @Column(name="product_id")
    Integer productId;

    @Column(name="quantity_purchased")
    Integer quantityPurchased;


    //Constructors
    public CartItem() {}

    public CartItem(Product product) {
        if (product != null) {
            this.productId = product.getProductId();
        }
    }


    public CartItem(Integer productId) {
        this.productId = productId;
    }


    //Getters/setters
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }


    public Integer getQuantityPurchased() {
        return quantityPurchased;
    }

    public void setQuantityPurchased(Integer quantityPurchased) {
        this.quantityPurchased = quantityPurchased;
    }


    //Not sure if we need this here, was in the uml
    public Double subTotal(){

        return 0.00;
    }
}
