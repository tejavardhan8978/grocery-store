package edu.metro.grocerystore.repository;

import edu.metro.grocerystore.model.Order;
import edu.metro.grocerystore.model.OrderStatus;
import edu.metro.grocerystore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    // Find by order number
    Optional<Order> findByOrderNumber(String orderNumber);
    
    // Find all orders for a specific user
    Page<Order> findByUser(User user, Pageable pageable);
    
    List<Order> findByUser(User user);
    
    // Find orders by user and status
    Page<Order> findByUserAndOrderStatus(User user, OrderStatus status, Pageable pageable);
    
    // Find all orders by status
    Page<Order> findByOrderStatus(OrderStatus status, Pageable pageable);
    
    // Find orders by date range
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    Page<Order> findByDateRange(@Param("startDate") Instant startDate, 
                                 @Param("endDate") Instant endDate, 
                                 Pageable pageable);
    
    // Find orders by date range and status
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate AND o.orderStatus = :status")
    Page<Order> findByDateRangeAndStatus(@Param("startDate") Instant startDate, 
                                          @Param("endDate") Instant endDate,
                                          @Param("status") OrderStatus status,
                                          Pageable pageable);
    
    // Find orders by user and date range
    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.createdAt BETWEEN :startDate AND :endDate")
    Page<Order> findByUserAndDateRange(@Param("user") User user,
                                        @Param("startDate") Instant startDate, 
                                        @Param("endDate") Instant endDate, 
                                        Pageable pageable);
    
    // Find orders by user, date range and status
    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.createdAt BETWEEN :startDate AND :endDate AND o.orderStatus = :status")
    Page<Order> findByUserDateRangeAndStatus(@Param("user") User user,
                                              @Param("startDate") Instant startDate, 
                                              @Param("endDate") Instant endDate,
                                              @Param("status") OrderStatus status,
                                              Pageable pageable);
    
    // Search orders containing a product by name
    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi JOIN oi.product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Order> searchOrdersByProductName(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    // Search orders by user containing a product by name
    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi JOIN oi.product p WHERE o.user = :user AND LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Order> searchOrdersByUserAndProductName(@Param("user") User user, 
                                                  @Param("searchTerm") String searchTerm, 
                                                  Pageable pageable);
    
    // Count orders by status
    long countByOrderStatus(OrderStatus status);
    
    // Count orders by user
    long countByUser(User user);
}
