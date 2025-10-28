package edu.metro.grocerystore.model;

import jakarta.persistence.*;


@Entity
public class CartItem {

    @Id
    @Column(name="order_id")
    Integer orderId;

    @Column(name="product_id")
    Integer productId;

    @Column(name="quantity_purchased")
    Integer quantityPurchased;


    //Constructors
    public CartItem() {}


    //TODO: Not sure we can use it in this way
    public CartItem(Product product) {
        //this.productId = product.productID;

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
