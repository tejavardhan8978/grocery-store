package edu.metro.grocerystore.service;

import edu.metro.grocerystore.model.*;
import edu.metro.grocerystore.repository.OrderItemRepository;
import edu.metro.grocerystore.repository.OrderRepository;
import edu.metro.grocerystore.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    @Autowired
    public OrderService(OrderRepository orderRepository, 
                       OrderItemRepository orderItemRepository,
                       ProductRepository productRepository,
                       CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    /**
     * Create order from cart
     */
    @Transactional
    public Order createOrderFromCart(User user, String storeLocation) {
        if (user == null || user.isGuest()) {
            throw new IllegalArgumentException("User must be logged in to create an order");
        }

        Cart cart = cartService.getOrCreateCart(user);
        
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        // Create new order
        Order order = new Order(user, OrderStatus.ACTIVE, storeLocation);
        
        // Convert cart items to order items
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            
            // Check stock availability
            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }
            
            OrderItem orderItem = new OrderItem(
                order,
                product,
                cartItem.getQuantity(),
                product.getPrice()
            );
            
            order.addOrderItem(orderItem);
            
            // Reduce stock
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }
        
        // Save order
        Order savedOrder = orderRepository.save(order);
        
        // Clear cart
        cartService.clearCart(user);
        
        return savedOrder;
    }

    /**
     * Get order by ID
     */
    public Optional<Order> getOrderById(Integer orderId) {
        return orderRepository.findById(orderId);
    }

    /**
     * Get order by order number
     */
    public Optional<Order> getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    /**
     * Get all orders (for admin/employee)
     */
    public Page<Order> getAllOrders(int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderRepository.findAll(pageable);
    }

    /**
     * Get orders by user
     */
    public Page<Order> getOrdersByUser(User user, int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderRepository.findByUser(user, pageable);
    }

    /**
     * Get orders by status (for admin/employee)
     */
    public Page<Order> getOrdersByStatus(OrderStatus status, int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderRepository.findByOrderStatus(status, pageable);
    }

    /**
     * Get orders by user and status
     */
    public Page<Order> getOrdersByUserAndStatus(User user, OrderStatus status, int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderRepository.findByUserAndOrderStatus(user, status, pageable);
    }

    /**
     * Get orders by date range (for admin/employee)
     */
    public Page<Order> getOrdersByDateRange(LocalDate startDate, LocalDate endDate, int page, int size, String sortBy, String sortDirection) {
        Instant start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        
        Sort sort = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return orderRepository.findByDateRange(start, end, pageable);
    }

    /**
     * Get orders by user and date range
     */
    public Page<Order> getOrdersByUserAndDateRange(User user, LocalDate startDate, LocalDate endDate, int page, int size, String sortBy, String sortDirection) {
        Instant start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        
        Sort sort = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return orderRepository.findByUserAndDateRange(user, start, end, pageable);
    }

    /**
     * Search orders by product name (for admin/employee)
     */
    public Page<Order> searchOrdersByProductName(String searchTerm, int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderRepository.searchOrdersByProductName(searchTerm, pageable);
    }

    /**
     * Search orders by user and product name
     */
    public Page<Order> searchOrdersByUserAndProductName(User user, String searchTerm, int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderRepository.searchOrdersByUserAndProductName(user, searchTerm, pageable);
    }

    /**
     * Update order status (admin/employee only)
     */
    @Transactional
    public Order updateOrderStatus(Integer orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        // Validate status transitions
        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot update status of a cancelled order");
        }
        
        if (order.getOrderStatus() == OrderStatus.COMPLETED && newStatus != OrderStatus.COMPLETED) {
            throw new IllegalArgumentException("Cannot change status of a completed order");
        }
        
        order.setOrderStatus(newStatus);
        order.setUpdatedAt(Instant.now());
        
        return orderRepository.save(order);
    }
    
    /**
     * Update order details
     */
    @Transactional
    public Order updateOrder(Order order) {
        order.setUpdatedAt(Instant.now());
        return orderRepository.save(order);
    }

    /**
     * Cancel order
     */
    @Transactional
    public Order cancelOrder(Integer orderId, User user) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        // Verify user owns the order or is admin/employee
        if (!order.getUser().getId().equals(user.getId()) && !user.isAdmin() && !user.isEmployee()) {
            throw new IllegalArgumentException("You don't have permission to cancel this order");
        }
        
        // Can only cancel active or ready to pickup orders
        if (order.getOrderStatus() != OrderStatus.ACTIVE && order.getOrderStatus() != OrderStatus.READY_TO_PICKUP) {
            throw new IllegalArgumentException("Only active or ready to pickup orders can be cancelled");
        }
        
        // Restore stock for all items
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
        }
        
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(Instant.now());
        
        return orderRepository.save(order);
    }

    /**
     * Reorder - creates a new order based on an existing order
     */
    @Transactional
    public Order reorder(Integer orderId, User user, String storeLocation) {
        Order existingOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        // Verify user owns the order
        if (!existingOrder.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You don't have permission to reorder this order");
        }
        
        // Create new order
        Order newOrder = new Order(user, OrderStatus.ACTIVE, storeLocation);
        
        // Copy items from existing order
        for (OrderItem existingItem : existingOrder.getOrderItems()) {
            Product product = existingItem.getProduct();
            
            // Check if product is still available
            if (!product.isAvailable()) {
                continue; // Skip unavailable products
            }
            
            // Check stock
            if (product.getQuantity() < existingItem.getQuantity()) {
                // Add what's available
                if (product.getQuantity() > 0) {
                    OrderItem newItem = new OrderItem(
                        newOrder,
                        product,
                        product.getQuantity(),
                        product.getPrice()
                    );
                    newOrder.addOrderItem(newItem);
                    product.setQuantity(0);
                    productRepository.save(product);
                }
            } else {
                OrderItem newItem = new OrderItem(
                    newOrder,
                    product,
                    existingItem.getQuantity(),
                    product.getPrice()
                );
                newOrder.addOrderItem(newItem);
                product.setQuantity(product.getQuantity() - existingItem.getQuantity());
                productRepository.save(product);
            }
        }
        
        if (newOrder.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("None of the products from the original order are currently available");
        }
        
        return orderRepository.save(newOrder);
    }

    /**
     * Get order count by status
     */
    public long getOrderCountByStatus(OrderStatus status) {
        return orderRepository.countByOrderStatus(status);
    }

    /**
     * Get order count by user
     */
    public long getOrderCountByUser(User user) {
        return orderRepository.countByUser(user);
    }
}
