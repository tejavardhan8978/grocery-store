package edu.metro.grocerystore.controller;

import edu.metro.grocerystore.model.ProductCategory;
import edu.metro.grocerystore.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * Base controller that provides common model attributes for all controllers
 */
public abstract class BaseController {
    
    @Autowired
    protected ProductCategoryService productCategoryService;
    
    /**
     * Add product categories to all views
     * This method is automatically called for all controller methods
     */
    @ModelAttribute("productCategories")
    public List<ProductCategory> addProductCategories() {
        return productCategoryService.getAllCategories();
    }
}