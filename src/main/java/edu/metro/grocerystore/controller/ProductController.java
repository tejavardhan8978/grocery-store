package edu.metro.grocerystore.controller;

import edu.metro.grocerystore.DTO.ProductSearchCriteria;
import edu.metro.grocerystore.model.Product;
import edu.metro.grocerystore.model.ProductCategory;
import edu.metro.grocerystore.model.User;
import edu.metro.grocerystore.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
// image responses removed; images are served from static resources
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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
     * Helper method to check if current user is admin or employee
     */
    private boolean isAdminOrEmployee(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        return loggedInUser != null && (loggedInUser.isAdmin() || loggedInUser.isEmployee());
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
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean inStock,
            HttpSession session,
            Model model) {
        
        Page<Product> productsPage;
        boolean isAdminOrEmployee = isAdminOrEmployee(session);
        
        // Admin/Employee can see all products, customers see only active products
        if (isAdminOrEmployee) {
            // Search with all filters including isActive and inStock
            productsPage = productService.searchAllProducts(search, categoryId, minPrice, maxPrice,
                                                           isActive, inStock, page, size, sortBy, sortDirection);
        } else {
            // Customers only see active, in-stock products
            if (search != null || categoryId != null || minPrice != null || maxPrice != null) {
                ProductSearchCriteria criteria = new ProductSearchCriteria(search, categoryId, minPrice, maxPrice,
                                                                           page, size, sortBy, sortDirection);
                productsPage = productService.searchProducts(criteria);
            } else {
                productsPage = productService.getAllActiveProducts(page, size, sortBy, sortDirection);
            }
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
        model.addAttribute("isActive", isActive);
        model.addAttribute("inStock", inStock);
        model.addAttribute("isAdminOrEmployee", isAdminOrEmployee);
        
        // Add categories for filter dropdown
        List<ProductCategory> categories = productCategoryService.getAllCategories();
        model.addAttribute(CATEGORIES_ATTR, categories);
        
        // Get max price for price slider
        BigDecimal maxPriceAvailable = productService.getMaxPrice();
        model.addAttribute("maxPriceAvailable", maxPriceAvailable);
        
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
    public String getProductDetails(@PathVariable Integer id, Model model, HttpSession session) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isEmpty()) {
            return "redirect:/products";
        }
        
        Product product = productOpt.get();
        model.addAttribute("product", product);
        model.addAttribute("isAdminOrEmployee", isAdminOrEmployee(session));
        
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
    
    /**
     * Update product quantity (Admin/Employee only)
     */
    @PostMapping("/products/{id}/update-quantity")
    public String updateProductQuantity(@PathVariable Integer id, 
                                       @RequestParam Integer quantity,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {
        if (!isAdminOrEmployee(session)) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access");
            return "redirect:/products/" + id;
        }
        
        try {
            Optional<Product> productOpt = productService.getProductById(id);
            if (productOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Product not found");
                return "redirect:/products";
            }
            
            Product product = productOpt.get();
            product.setQuantity(quantity);
            productService.updateProduct(product);
            
            redirectAttributes.addFlashAttribute("success", "Product quantity updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update product quantity: " + e.getMessage());
        }
        
        return "redirect:/products/" + id;
    }
    
    /**
     * Toggle product active status (Admin/Employee only)
     */
    @PostMapping("/products/{id}/toggle-active")
    public String toggleProductActive(@PathVariable Integer id,
                                      HttpSession session, 
                                      RedirectAttributes redirectAttributes) {
        if (!isAdminOrEmployee(session)) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access");
            return "redirect:/products/" + id;
        }
        
        try {
            Optional<Product> productOpt = productService.getProductById(id);
            if (productOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Product not found");
                return "redirect:/products";
            }
            
            Product product = productOpt.get();
            product.setIsActive(!product.getIsActive());
            productService.updateProduct(product);
            
            String status = product.getIsActive() ? "activated" : "deactivated";
            redirectAttributes.addFlashAttribute("success", "Product " + status + " successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update product status: " + e.getMessage());
        }
        
        return "redirect:/products/" + id;
    }
    
    // Product images are served from static resources under /images
    
    /**
     * Display edit product form (Admin only)
     */
    @GetMapping("/admin/products/{id}/edit")
    public String showEditProductForm(@PathVariable Integer id, Model model, HttpSession session) {
        if (!isAdminOrEmployee(session)) {
            return "redirect:/products";
        }
        
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isEmpty()) {
            return "redirect:/products";
        }
        
        model.addAttribute("product", productOpt.get());
        List<ProductCategory> categories = productCategoryService.getAllCategories();
        model.addAttribute(CATEGORIES_ATTR, categories);
        
        return "admin/edit-product";
    }
    
    /**
     * Handle product update (Admin only)
     */
    @PostMapping("/admin/products/{id}/edit")
    public String updateProduct(@PathVariable Integer id,
                               @RequestParam String name,
                               @RequestParam String sku,
                               @RequestParam BigDecimal price,
                               @RequestParam Integer quantity,
                               @RequestParam(required = false) Integer reorderLevel,
                               @RequestParam(required = false) String description,
                               @RequestParam(required = false) String imageUrl,
                               @RequestParam Integer categoryId,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (!isAdminOrEmployee(session)) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access");
            return "redirect:/products";
        }
        
        try {
            Optional<Product> productOpt = productService.getProductById(id);
            if (productOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Product not found");
                return "redirect:/products";
            }
            
            Product product = productOpt.get();
            
            // Check if SKU is being changed and if it already exists for another product
            if (!product.getSku().equals(sku)) {
                Optional<Product> existingProduct = productService.getProductBySku(sku);
                if (existingProduct.isPresent() && !existingProduct.get().getProductId().equals(id)) {
                    redirectAttributes.addFlashAttribute("error", "Product with SKU " + sku + " already exists");
                    return "redirect:/admin/products/" + id + "/edit";
                }
            }
            
            // Get category
            Optional<ProductCategory> categoryOpt = productCategoryService.findById(categoryId);
            if (categoryOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Invalid category selected");
                return "redirect:/admin/products/" + id + "/edit";
            }
            
            // Update product
            product.setName(name);
            product.setSku(sku);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setReorderLevel(reorderLevel);
            product.setDescription(description);
            product.setImageUrl(imageUrl);
            product.setCategory(categoryOpt.get());
            
            productService.updateProduct(product);
            redirectAttributes.addFlashAttribute("success", "Product updated successfully");
            return "redirect:/products/" + product.getProductId();
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update product: " + e.getMessage());
            return "redirect:/admin/products/" + id + "/edit";
        }
    }
    
    /**
     * Display add product form (Admin only)
     */
    @GetMapping("/admin/products/add")
    public String showAddProductForm(Model model, HttpSession session) {
        if (!isAdminOrEmployee(session)) {
            return "redirect:/products";
        }
        
        model.addAttribute("product", new Product());
        List<ProductCategory> categories = productCategoryService.getAllCategories();
        model.addAttribute(CATEGORIES_ATTR, categories);
        
        return "admin/add-product";
    }
    
    /**
     * Handle product creation (Admin only)
     */
    @PostMapping("/admin/products/add")
    public String addProduct(@RequestParam String name,
                            @RequestParam String sku,
                            @RequestParam BigDecimal price,
                            @RequestParam Integer quantity,
                            @RequestParam(required = false) Integer reorderLevel,
                            @RequestParam(required = false) String description,
                            @RequestParam(required = false) String imageUrl,
                            @RequestParam Integer categoryId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        if (!isAdminOrEmployee(session)) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access");
            return "redirect:/products";
        }
        
        try {
            // Check if SKU already exists
            if (productService.getProductBySku(sku).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Product with SKU " + sku + " already exists");
                return "redirect:/admin/products/add";
            }
            
            // Get category
            Optional<ProductCategory> categoryOpt = productCategoryService.findById(categoryId);
            if (categoryOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Invalid category selected");
                return "redirect:/admin/products/add";
            }
            
            // Create product
            Product product = new Product();
            product.setName(name);
            product.setSku(sku);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setReorderLevel(reorderLevel);
            product.setDescription(description);
            product.setImageUrl(imageUrl);
            product.setCategory(categoryOpt.get());
            product.setIsActive(true);
            
            Product savedProduct = productService.createProduct(product);
            redirectAttributes.addFlashAttribute("success", "Product added successfully");
            return "redirect:/products/" + savedProduct.getProductId();
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add product: " + e.getMessage());
            return "redirect:/admin/products/add";
        }
    }
}