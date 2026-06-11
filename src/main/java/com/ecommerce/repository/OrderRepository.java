package com.ecommerce.repository;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUserOrderByCreatedAtDesc(User user);

	List<Order> findByStatusIn(List<Order.OrderStatus> statuses);

	Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);

	Optional<Order> findByOrderNumber(String orderNumber);

	long countByStatus(Order.OrderStatus status);

	List<Order> findByAssignedToAndStatusIn(User assignedTo, List<Order.OrderStatus> statuses);

	List<Order> findByAssignedTo(User assignedTo);
}