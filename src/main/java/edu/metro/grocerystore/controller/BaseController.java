package edu.metro.grocerystore.controller;

import edu.metro.grocerystore.model.ProductCategory;
import edu.metro.grocerystore.model.User;
import edu.metro.grocerystore.service.CartService;
import edu.metro.grocerystore.service.ProductCategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * Base controller that provides common model attributes for all controllers
 */
public abstract class BaseController {
    
    @Autowired
    protected ProductCategoryService productCategoryService;
    
    @Autowired
    protected CartService cartService;
    
    /**
     * Add product categories to all views
     * This method is automatically called for all controller methods
     */
    @ModelAttribute("productCategories")
    public List<ProductCategory> addProductCategories() {
        return productCategoryService.getAllCategories();
    }
    
    /**
     * Add cart item count to all views
     * This method is automatically called for all controller methods
     */
    @ModelAttribute("cartItemCount")
    public int addCartItemCount(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || loggedInUser.isGuest()) {
            return 0;
        }
        return cartService.getCartItemCount(loggedInUser);
    }
}