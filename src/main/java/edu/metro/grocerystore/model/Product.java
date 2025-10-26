package edu.metro.grocerystore.model;

import jakarta.persistence.*;

@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer productID;

    @Column(name="product_name")
    String name;

    @ManyToOne
    @JoinColumn(name="cart_id", nullable=false)
    private Cart cart;







}
