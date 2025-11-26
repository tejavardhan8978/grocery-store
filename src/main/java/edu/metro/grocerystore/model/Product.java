package edu.metro.grocerystore.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
    private Integer productId;

    @Column(name="name")
    private String name;

    @Column(name="sku")
    private String sku;
    
    @Column(name="price")
    private BigDecimal price;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="reorder_level")
    private Integer reorderLevel;

    @Column(name="description")
    private String description;

    @Column(name="image_url")
    private String imageUrl;
    
    // Images are served from static resources now; keep only imageUrl

    @Column(name="is_active")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id", referencedColumnName="category_id")
    private ProductCategory category;

    // Constructors
    public Product() {}

    public Product(String name, String sku, BigDecimal price, Integer quantity, ProductCategory category) {
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.isActive = true;
    }

    // Getters and Setters
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    // Business methods
    public boolean adjustStock(Integer quantityChange) {
        if (this.quantity + quantityChange < 0) {
            return false; // Cannot reduce stock below zero
        }
        this.quantity += quantityChange;
        return true;
    }

    public boolean isInStock() {
        return this.quantity != null && this.quantity > 0;
    }

    public boolean isLowStock() {
        return this.quantity != null && this.reorderLevel != null && this.quantity <= this.reorderLevel;
    }

    public boolean isAvailable() {
        return this.isActive != null && this.isActive && isInStock();
    }
}
