package edu.metro.grocerystore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.math.BigDecimal;

public class OrderItem {

    @Id
    @Column(name="order_id")
    Integer orderId;

    @Column(name="product_id")
    Integer productId;

    @Column(name="quantity_purchased")
    Integer quantityPurchased;

    @Column(name="itemPrice")
    BigDecimal itemPrice;


}
