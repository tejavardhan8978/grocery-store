package edu.metro.grocerystore.controller;

import edu.metro.grocerystore.DTO.ProductSearchCriteria;
import edu.metro.grocerystore.model.Product;
import edu.metro.grocerystore.model.ProductCategory;
import edu.metro.grocerystore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController extends BaseController {
    
    // Constants for model attribute names
    private static final String PRODUCTS_ATTR = "products";
    private static final String CURRENT_PAGE_ATTR = "currentPage";
    private static final String TOTAL_PAGES_ATTR = "totalPages";
    private static final String TOTAL_ELEMENTS_ATTR = "totalElements";
    private static final String SORT_BY_ATTR = "sortBy";
    private static final String SORT_DIRECTION_ATTR = "sortDirection";
    private static final String SEARCH_ATTR = "search";
    private static final String CATEGORIES_ATTR = "categories";
    private static final String PRODUCTS_LIST_VIEW = "products/list";
    
    private final ProductService productService;
    
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    /**
     * Display all products
     */
    @GetMapping("/products")
    public String getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Model model) {
        
        Page<Product> productsPage;
        
        // If search or filters are provided, use search functionality
        if (search != null || categoryId != null || minPrice != null || maxPrice != null) {
            ProductSearchCriteria criteria = new ProductSearchCriteria(search, categoryId, minPrice, maxPrice,
                                                                       page, size, sortBy, sortDirection);
            productsPage = productService.searchProducts(criteria);
        } else {
            productsPage = productService.getAllActiveProducts(page, size, sortBy, sortDirection);
        }
        
        model.addAttribute(PRODUCTS_ATTR, productsPage.getContent());
        model.addAttribute(CURRENT_PAGE_ATTR, page);
        model.addAttribute(TOTAL_PAGES_ATTR, productsPage.getTotalPages());
        model.addAttribute(TOTAL_ELEMENTS_ATTR, productsPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute(SORT_BY_ATTR, sortBy);
        model.addAttribute(SORT_DIRECTION_ATTR, sortDirection);
        model.addAttribute(SEARCH_ATTR, search);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        
        // Add categories for filter dropdown
        List<ProductCategory> categories = productCategoryService.getAllCategories();
        model.addAttribute(CATEGORIES_ATTR, categories);
        
        return PRODUCTS_LIST_VIEW;
    }
    
    /**
     * Display products by category
     */
    @GetMapping("/products/category/{categoryId}")
    public String getProductsByCategory(
            @PathVariable Integer categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Model model) {
        
        // Get category information
        Optional<ProductCategory> categoryOpt = productCategoryService.findById(categoryId);
        if (categoryOpt.isEmpty()) {
            return "redirect:/products";
        }
        
        ProductCategory category = categoryOpt.get();
        
        Page<Product> productsPage;
        
        // If additional filters are provided, use search functionality
        if (search != null || minPrice != null || maxPrice != null) {
            ProductSearchCriteria criteria = new ProductSearchCriteria(search, categoryId, minPrice, maxPrice,
                                                                       page, size, sortBy, sortDirection);
            productsPage = productService.searchProducts(criteria);
        } else {
            productsPage = productService.getProductsByCategory(categoryId, page, size, sortBy, sortDirection);
        }
        
        model.addAttribute(PRODUCTS_ATTR, productsPage.getContent());
        model.addAttribute(CURRENT_PAGE_ATTR, page);
        model.addAttribute(TOTAL_PAGES_ATTR, productsPage.getTotalPages());
        model.addAttribute(TOTAL_ELEMENTS_ATTR, productsPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute(SORT_BY_ATTR, sortBy);
        model.addAttribute(SORT_DIRECTION_ATTR, sortDirection);
        model.addAttribute(SEARCH_ATTR, search);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        
        // Add categories for filter dropdown
        List<ProductCategory> categories = productCategoryService.getAllCategories();
        model.addAttribute(CATEGORIES_ATTR, categories);
        
        return PRODUCTS_LIST_VIEW;
    }
    
    /**
     * Display product details
     */
    @GetMapping("/products/{id}")
    public String getProductDetails(@PathVariable Integer id, Model model) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isEmpty()) {
            return "redirect:/products";
        }
        
        Product product = productOpt.get();
        model.addAttribute("product", product);
        
        // Get related products from the same category
        List<Product> relatedProducts = productService.getProductsByCategory(product.getCategory().getCategoryId());
        // Remove current product from related products and limit to 4
        relatedProducts = relatedProducts.stream()
                .filter(p -> !p.getProductId().equals(id))
                .limit(4)
                .toList();
        model.addAttribute("relatedProducts", relatedProducts);
        
        return "products/detail";
    }
    
    /**
     * Search products
     */
    @GetMapping("/products/search")
    public String searchProducts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            Model model) {
        
        ProductSearchCriteria criteria = new ProductSearchCriteria(q, null, null, null, 
                                                                page, size, sortBy, sortDirection);
        Page<Product> productsPage = productService.searchProducts(criteria);
        
        model.addAttribute(PRODUCTS_ATTR, productsPage.getContent());
        model.addAttribute(CURRENT_PAGE_ATTR, page);
        model.addAttribute(TOTAL_PAGES_ATTR, productsPage.getTotalPages());
        model.addAttribute(TOTAL_ELEMENTS_ATTR, productsPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute(SORT_BY_ATTR, sortBy);
        model.addAttribute(SORT_DIRECTION_ATTR, sortDirection);
        model.addAttribute(SEARCH_ATTR, q);
        model.addAttribute("searchQuery", q);
        
        // Add categories for filter dropdown
        List<ProductCategory> categories = productCategoryService.getAllCategories();
        model.addAttribute(CATEGORIES_ATTR, categories);
        
        return PRODUCTS_LIST_VIEW;
    }
    
    /**
     * Get featured products for home page
     */
    @GetMapping("/api/products/featured")
    public String getFeaturedProducts(@RequestParam(defaultValue = "8") int limit, Model model) {
        List<Product> featuredProducts = productService.getFeaturedProducts(limit);
        model.addAttribute("featuredProducts", featuredProducts);
        return "fragments/featured-products";
    }
}