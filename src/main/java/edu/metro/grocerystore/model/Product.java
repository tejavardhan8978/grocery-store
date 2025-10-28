package edu.metro.grocerystore.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
    Integer productID;

    @Column(name="name")
    String name;

    @Column(name="sku")
    String sku;
    
    @Column(name="price")
    BigDecimal price;

    @Column(name="quantity")
    Integer quantity;

    @Column(name="reorder_level")
    Integer reorderLevel;

    @ManyToOne
    @JoinColumn(name="cart_id", nullable=false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name="category_name", nullable=false)
    private ProductCategory category;

    public boolean adjustStock(Integer quantity){
         return false;
    }





}
