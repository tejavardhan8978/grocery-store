package edu.metro.grocerystore.controller;

import edu.metro.grocerystore.model.CartItem;
import edu.metro.grocerystore.model.Order;
import edu.metro.grocerystore.model.User;
import edu.metro.grocerystore.service.CartService;
import edu.metro.grocerystore.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class CheckoutController extends BaseController {

    private static final String LOGGED_IN_USER_ATTR = "loggedInUser";
    private static final String REDIRECT_LOGIN = "redirect:/login";

    private final CartService cartService;
    private final OrderService orderService;

    @Autowired
    public CheckoutController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    /**
     * Display checkout page
     */
    @GetMapping("/checkout")
    public String showCheckoutPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);

        if (loggedInUser == null || loggedInUser.isGuest()) {
            return REDIRECT_LOGIN;
        }

        // Get cart items
        List<CartItem> cartItems = cartService.getCartItems(loggedInUser);
        
        if (cartItems == null || cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        // Calculate totals
        BigDecimal cartTotal = cartService.getCartTotal(loggedInUser);
        int cartItemCount = cartService.getCartItemCount(loggedInUser);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartTotal);
        model.addAttribute("cartItemCount", cartItemCount);

        return "checkout";
    }
    
    /**
     * Display order confirmation page
     */
    @GetMapping("/order-confirmation")
    public String showOrderConfirmation(
            @RequestParam(required = false) Integer orderId,
            @RequestParam(defaultValue = "false") boolean success,
            HttpSession session,
            Model model) {
        
        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);

        if (loggedInUser == null || loggedInUser.isGuest()) {
            return REDIRECT_LOGIN;
        }

        model.addAttribute("success", success);
        
        if (success && orderId != null) {
            Optional<Order> orderOpt = orderService.getOrderById(orderId);
            if (orderOpt.isPresent()) {
                model.addAttribute("order", orderOpt.get());
            }
        }

        return "order-confirmation";
    }
}
