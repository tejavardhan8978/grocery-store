package edu.metro.grocerystore.model;

import jakarta.persistence.*;


@Entity
@Table(name="product_categories")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer category_id;

    @Column(name="category_name")
    String categoryName;

}

