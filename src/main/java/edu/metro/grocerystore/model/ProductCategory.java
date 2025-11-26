package edu.metro.grocerystore.model;

import jakarta.persistence.*;


@Entity
@Table(name="product_categories")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")
    private Integer categoryId;

    @Column(name="category_name")
    private String categoryName;

    @Column(name="category_image_url")
    private String categoryImageUrl;

    @Column(name="category_icon_svg")
    private String categoryIconSvg;

    @Column(name="category_description")
    private String categoryDescription;

    public ProductCategory() {}

    public ProductCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public ProductCategory(String categoryName, String categoryImageUrl, String categoryIconSvg) {
        this.categoryName = categoryName;
        this.categoryImageUrl = categoryImageUrl;
        this.categoryIconSvg = categoryIconSvg;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImageUrl() {
        return categoryImageUrl;
    }

    public void setCategoryImageUrl(String categoryImageUrl) {
        this.categoryImageUrl = categoryImageUrl;
    }

    public String getCategoryIconSvg() {
        return categoryIconSvg;
    }

    public void setCategoryIconSvg(String categoryIconSvg) {
        this.categoryIconSvg = categoryIconSvg;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }
    
    // Convenience method for templates
    public String getName() {
        return categoryName;
    }
}

