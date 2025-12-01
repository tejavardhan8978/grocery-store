package edu.metro.grocerystore.controller;

import edu.metro.grocerystore.model.Cart;
import edu.metro.grocerystore.model.User;
import edu.metro.grocerystore.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {
    
    private static final String LOGGED_IN_USER_ATTR = "loggedInUser";
    private static final String REDIRECT_LOGIN = "redirect:/login";
    private static final String REDIRECT_CART = "redirect:/cart";
    
    private final CartService cartService;
    
    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    
    /**
     * Display cart page
     */
    @GetMapping
    public String viewCart(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);
        
        if (loggedInUser == null || loggedInUser.isGuest()) {
            redirectAttributes.addFlashAttribute("error", "Please log in to view your cart.");
            return REDIRECT_LOGIN;
        }
        
        Cart cart = cartService.getOrCreateCart(loggedInUser);
        
        // BaseController automatically adds user to model
        model.addAttribute("cart", cart);
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("cartTotal", cart.getTotal());
        model.addAttribute("cartItemCount", cart.getTotalItems());
        
        return "cart";
    }
    
    /**
     * Add product to cart
     */
    @PostMapping("/add")
    public String addToCart(
            @RequestParam Integer productId,
            @RequestParam(defaultValue = "1") Integer quantity,
            @RequestParam(required = false) String returnUrl,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);
        
        if (loggedInUser == null || loggedInUser.isGuest()) {
            redirectAttributes.addFlashAttribute("error", "Please log in to add items to your cart.");
            return REDIRECT_LOGIN;
        }
        
        try {
            cartService.addProductToCart(loggedInUser, productId, quantity);
            redirectAttributes.addFlashAttribute("success", "Product added to cart successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding product to cart. Please try again.");
        }
        
        // Redirect back to the page the user came from, or to cart
        if (returnUrl != null && !returnUrl.isEmpty()) {
            return "redirect:" + returnUrl;
        }
        return REDIRECT_CART;
    }
    
    /**
     * Update cart item quantity
     */
    @PostMapping("/update/{cartItemId}")
    public String updateCartItem(
            @PathVariable Integer cartItemId,
            @RequestParam Integer quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);
        
        if (loggedInUser == null || loggedInUser.isGuest()) {
            redirectAttributes.addFlashAttribute("error", "Please log in to update your cart.");
            return REDIRECT_LOGIN;
        }
        
        try {
            cartService.updateCartItemQuantity(loggedInUser, cartItemId, quantity);
            redirectAttributes.addFlashAttribute("success", "Cart updated successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating cart. Please try again.");
        }
        
        return REDIRECT_CART;
    }
    
    /**
     * Remove item from cart
     */
    @PostMapping("/remove/{cartItemId}")
    public String removeFromCart(
            @PathVariable Integer cartItemId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);
        
        if (loggedInUser == null || loggedInUser.isGuest()) {
            redirectAttributes.addFlashAttribute("error", "Please log in to modify your cart.");
            return REDIRECT_LOGIN;
        }
        
        try {
            cartService.removeItemFromCart(loggedInUser, cartItemId);
            redirectAttributes.addFlashAttribute("success", "Item removed from cart.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error removing item from cart. Please try again.");
        }
        
        return REDIRECT_CART;
    }
    
    /**
     * Clear all items from cart
     */
    @PostMapping("/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);
        
        if (loggedInUser == null || loggedInUser.isGuest()) {
            redirectAttributes.addFlashAttribute("error", "Please log in to clear your cart.");
            return REDIRECT_LOGIN;
        }
        
        try {
            cartService.clearCart(loggedInUser);
            redirectAttributes.addFlashAttribute("success", "Cart cleared successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error clearing cart. Please try again.");
        }
        
        return REDIRECT_CART;
    }
    
    /**
     * Get cart item count (for AJAX requests)
     */
    @GetMapping("/count")
    @ResponseBody
    public int getCartItemCount(HttpSession session) {
        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);
        
        if (loggedInUser == null || loggedInUser.isGuest()) {
            return 0;
        }
        
        return cartService.getCartItemCount(loggedInUser);
    }
}
