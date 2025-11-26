package edu.metro.grocerystore.repository;

import edu.metro.grocerystore.model.Order;
import edu.metro.grocerystore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    
    List<OrderItem> findByOrder(Order order);
    
    void deleteByOrder(Order order);
}
