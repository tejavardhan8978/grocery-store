package edu.metro.grocerystore.controller;

import edu.metro.grocerystore.model.Order;
import edu.metro.grocerystore.model.OrderStatus;
import edu.metro.grocerystore.model.User;
import edu.metro.grocerystore.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController extends BaseController {

    private static final String LOGGED_IN_USER_ATTR = "loggedInUser";
    private static final String REDIRECT_LOGIN = "redirect:/login";
    private static final String REDIRECT_ORDERS = "redirect:/orders";

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Display orders list page
     */
    @GetMapping
    public String viewOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String search,
            HttpSession session,
            Model model) {

        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);

        if (loggedInUser == null || loggedInUser.isGuest()) {
            return REDIRECT_LOGIN;
        }

        Page<Order> ordersPage;
        OrderStatus orderStatus = null;

        // Parse status if provided
        if (status != null && !status.isEmpty()) {
            try {
                orderStatus = OrderStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                // Invalid status, ignore
            }
        }

        // Determine which orders to show based on user role
        boolean isAdminOrEmployee = loggedInUser.isAdmin() || loggedInUser.isEmployee();

        // Apply filters and search
        if (search != null && !search.trim().isEmpty()) {
            // Search by product name
            if (isAdminOrEmployee) {
                ordersPage = orderService.searchOrdersByProductName(search, page, size, sortBy, sortDirection);
            } else {
                ordersPage = orderService.searchOrdersByUserAndProductName(loggedInUser, search, page, size, sortBy, sortDirection);
            }
        } else if (startDate != null && endDate != null) {
            // Filter by date range
            if (isAdminOrEmployee) {
                ordersPage = orderService.getOrdersByDateRange(startDate, endDate, page, size, sortBy, sortDirection);
            } else {
                ordersPage = orderService.getOrdersByUserAndDateRange(loggedInUser, startDate, endDate, page, size, sortBy, sortDirection);
            }
        } else if (orderStatus != null) {
            // Filter by status
            if (isAdminOrEmployee) {
                ordersPage = orderService.getOrdersByStatus(orderStatus, page, size, sortBy, sortDirection);
            } else {
                ordersPage = orderService.getOrdersByUserAndStatus(loggedInUser, orderStatus, page, size, sortBy, sortDirection);
            }
        } else {
            // No filters - show all orders
            if (isAdminOrEmployee) {
                ordersPage = orderService.getAllOrders(page, size, sortBy, sortDirection);
            } else {
                ordersPage = orderService.getOrdersByUser(loggedInUser, page, size, sortBy, sortDirection);
            }
        }

        // Add attributes to model
        model.addAttribute("user", loggedInUser);
        model.addAttribute("orders", ordersPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        model.addAttribute("totalElements", ordersPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("status", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("search", search);
        model.addAttribute("isAdminOrEmployee", isAdminOrEmployee);
        model.addAttribute("orderStatuses", OrderStatus.values());

        return "orders/list";
    }

    /**
     * Display order details page
     */
    @GetMapping("/{orderId}")
    public String viewOrderDetails(
            @PathVariable Integer orderId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);

        if (loggedInUser == null || loggedInUser.isGuest()) {
            return REDIRECT_LOGIN;
        }

        Optional<Order> orderOpt = orderService.getOrderById(orderId);

        if (orderOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Order not found");
            return REDIRECT_ORDERS;
        }

        Order order = orderOpt.get();

        // Check if user has permission to view this order
        boolean isAdminOrEmployee = loggedInUser.isAdmin() || loggedInUser.isEmployee();
        boolean isOwner = order.getUser().getId().equals(loggedInUser.getId());

        if (!isAdminOrEmployee && !isOwner) {
            redirectAttributes.addFlashAttribute("error", "You don't have permission to view this order");
            return REDIRECT_ORDERS;
        }

        model.addAttribute("user", loggedInUser);
        model.addAttribute("order", order);
        model.addAttribute("isAdminOrEmployee", isAdminOrEmployee);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("orderStatuses", OrderStatus.values());

        return "orders/detail";
    }

    /**
     * Update order status (admin/employee only)
     */
    @PostMapping("/{orderId}/status")
    public String updateOrderStatus(
            @PathVariable Integer orderId,
            @RequestParam String newStatus,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);

        if (loggedInUser == null || loggedInUser.isGuest()) {
            return REDIRECT_LOGIN;
        }

        // Only admin and employee can update status
        if (!loggedInUser.isAdmin() && !loggedInUser.isEmployee()) {
            redirectAttributes.addFlashAttribute("error", "You don't have permission to update order status");
            return REDIRECT_ORDERS;
        }

        try {
            OrderStatus status = OrderStatus.valueOf(newStatus);
            orderService.updateOrderStatus(orderId, status);
            redirectAttributes.addFlashAttribute("success", "Order status updated successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating order status");
        }

        return "redirect:/orders/" + orderId;
    }

    /**
     * Cancel order
     */
    @PostMapping("/{orderId}/cancel")
    public String cancelOrder(
            @PathVariable Integer orderId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);

        if (loggedInUser == null || loggedInUser.isGuest()) {
            return REDIRECT_LOGIN;
        }

        try {
            orderService.cancelOrder(orderId, loggedInUser);
            redirectAttributes.addFlashAttribute("success", "Order cancelled successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error cancelling order");
        }

        return "redirect:/orders/" + orderId;
    }

    /**
     * Reorder
     */
    @PostMapping("/{orderId}/reorder")
    public String reorder(
            @PathVariable Integer orderId,
            @RequestParam(defaultValue = "Main Store") String storeLocation,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);

        if (loggedInUser == null || loggedInUser.isGuest()) {
            return REDIRECT_LOGIN;
        }

        try {
            Order newOrder = orderService.reorder(orderId, loggedInUser, storeLocation);
            redirectAttributes.addFlashAttribute("success", "Order created successfully from previous order");
            return "redirect:/orders/" + newOrder.getOrderId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating new order");
        }

        return "redirect:/orders/" + orderId;
    }

    /**
     * Create order from cart (checkout)
     */
    @PostMapping("/checkout")
    public String checkout(
            @RequestParam String storeLocation,
            @RequestParam(required = false) String billingAddress,
            @RequestParam(required = false) String notes,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute(LOGGED_IN_USER_ATTR);

        if (loggedInUser == null || loggedInUser.isGuest()) {
            redirectAttributes.addFlashAttribute("error", "Please log in to checkout");
            return REDIRECT_LOGIN;
        }

        try {
            // Simulate payment processing (not storing card details)
            // In a real application, this would integrate with a payment gateway
            
            Order order = orderService.createOrderFromCart(loggedInUser, storeLocation);
            
            // Set notes if provided
            if (notes != null && !notes.trim().isEmpty()) {
                order.setNotes(notes);
                orderService.updateOrder(order);
            }
            
            // Redirect to confirmation page
            return "redirect:/order-confirmation?orderId=" + order.getOrderId() + "&success=true";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        } catch (Exception e) {
            // Simulate payment failure
            redirectAttributes.addFlashAttribute("paymentError", "Payment processing failed. Please try again.");
            return "redirect:/order-confirmation?success=false";
        }
    }
}
