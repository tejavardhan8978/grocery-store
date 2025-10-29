package edu.metro.grocerystore.DTO;

import java.math.BigDecimal;

/**
 * DTO for product search criteria to reduce method parameter count
 */
public class ProductSearchCriteria {
    private String searchTerm;
    private Integer categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private int page;
    private int size;
    private String sortBy;
    private String sortDirection;
    
    public ProductSearchCriteria() {
        this.page = 0;
        this.size = 12;
        this.sortBy = "name";
        this.sortDirection = "ASC";
    }
    
    public ProductSearchCriteria(String searchTerm, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice,
                                int page, int size, String sortBy, String sortDirection) {
        this.searchTerm = searchTerm;
        this.categoryId = categoryId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
    }
    
    // Getters and setters
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    public Integer getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    public BigDecimal getMinPrice() {
        return minPrice;
    }
    
    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }
    
    public BigDecimal getMaxPrice() {
        return maxPrice;
    }
    
    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }
    
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public String getSortBy() {
        return sortBy;
    }
    
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
    
    public String getSortDirection() {
        return sortDirection;
    }
    
    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}