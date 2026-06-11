package com.ecommerce.service;

import com.ecommerce.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import com.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CartService cartService;

	@Autowired
	private UserService userService;

	@Transactional
	public Order placeOrder(String email, String shippingName, String shippingAddress, String shippingCity,
			String shippingState, String shippingZip, String shippingPhone, String paymentMethod, String deliverySlot) {

		User user = userService.findByEmail(email);

		List<CartItem> cartItems = cartService.getCartItems(email);

		if (cartItems.isEmpty()) {
			throw new RuntimeException("Cart is empty");
		}

		BigDecimal total = cartService.getCartTotal(cartItems);
		BigDecimal shipping = total.compareTo(new BigDecimal("500")) >= 0 ? BigDecimal.ZERO : new BigDecimal("49.00");

		List<OrderItem> orderItems = cartItems.stream().map(this::buildOrderItem).collect(Collectors.toList());

		String estimatedDeliveryDate = calculateEstimatedDelivery(deliverySlot);

		Order order = Order.builder().orderNumber(generateOrderNumber()).user(user).orderItems(orderItems)
				.totalAmount(total.add(shipping)).shippingCost(shipping).discount(BigDecimal.ZERO)
				.status(Order.OrderStatus.CONFIRMED)
				.paymentStatus(paymentMethod.equalsIgnoreCase("COD") ? Order.PaymentStatus.PENDING : Order.PaymentStatus.PAID)
				.paymentMethod(paymentMethod).shippingName(shippingName).shippingAddress(shippingAddress)
				.shippingCity(shippingCity).shippingState(shippingState).shippingZip(shippingZip)
				.shippingPhone(shippingPhone).deliverySlot(deliverySlot).estimatedDeliveryDate(estimatedDeliveryDate)
				.build();

		orderItems.forEach(item -> item.setOrder(order));
		Order savedOrder = orderRepository.save(order);
		cartService.clearCart(email);
		return savedOrder;
	}

	@Transactional
	public void cancelOrder(Long orderId, String email, String reason) {

	    Order order = findById(orderId);

	    if (!order.getUser().getEmail().equals(email)) {
	        throw new RuntimeException("Unauthorized action");
	    }
	    
	    if (order.getStatus() == Order.OrderStatus.SHIPPED     ||
	        order.getStatus() == Order.OrderStatus.DELIVERED   ||
	        order.getStatus() == Order.OrderStatus.CANCELLED   ||
	        order.getStatus() == Order.OrderStatus.RETURN_REQUESTED   ||
	        order.getStatus() == Order.OrderStatus.EXCHANGE_REQUESTED) {
	        throw new RuntimeException(
	                "Order cannot be cancelled at this stage");
	    }

	    order.setStatus(Order.OrderStatus.CANCELLED);

	    if (reason != null && !reason.isBlank()) {
	        order.setDeliveryNote("Cancelled by customer: " + reason);
	    }

	    orderRepository.save(order);
	}

	private OrderItem buildOrderItem(CartItem cartItem) {
		return OrderItem.builder().product(cartItem.getProduct()).quantity(cartItem.getQuantity())
				.unitPrice(cartItem.getProduct().getPrice())
				.subtotal(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
				.selectedSize(cartItem.getSelectedSize()) // ✅ NEW
				.build();
	}

	public List<Order> getOrdersByUser(String email) {
		User user = userService.findByEmail(email);
		return orderRepository.findByUserOrderByCreatedAtDesc(user);
	}

	public Order findById(Long id) {
		return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
	}

	public Order findByOrderNumber(String orderNumber) {
		return orderRepository.findByOrderNumber(orderNumber)
				.orElseThrow(() -> new RuntimeException("Order not found"));
	}

	public Page<Order> getAllOrders(int page, int size) {
		return orderRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
	}

	@Transactional
	public Order updateStatusByDelivery(Long id, Order.OrderStatus status, String deliveryNote) {

	    Order order = findById(id);

	    // 🚫 ADD THIS BLOCK HERE (VERY FIRST CHECK)
	    if (order.getStatus() == Order.OrderStatus.CANCELLED ||
	        order.getStatus() == Order.OrderStatus.DELIVERED) {
	        throw new RuntimeException("Order cannot be modified");
	    }

	    // ✅ Normal flow continues
	    order.setStatus(status);

	    if (deliveryNote != null && !deliveryNote.isBlank()) {
	        order.setDeliveryNote(deliveryNote);
	    }

	    if (order.getDeliveryPartnerName() == null) {
	        order.setDeliveryPartnerName("Delivery Partner");
	    }

	    if (status == Order.OrderStatus.SHIPPED) {
	        order.setDispatchedAt(LocalDateTime.now());
	    }

	    if (status == Order.OrderStatus.DELIVERED) {
	        order.setDeliveredAt(LocalDateTime.now());

	        if (order.getPaymentMethod().equalsIgnoreCase("COD")) {
	            order.setPaymentStatus(Order.PaymentStatus.PAID);
	        }
	    }

	    return orderRepository.save(order);
	}

	public List<Order> getOrdersForDelivery() {
		return orderRepository.findByStatusIn(
				List.of(Order.OrderStatus.CONFIRMED,
						Order.OrderStatus.PROCESSING,
						Order.OrderStatus.SHIPPED)
				);
	}
	
	// only returns orders assigned to THIS delivery partner
	public List<Order> getOrdersForDeliveryPartner(String email) {
	    User partner = userService.findByEmail(email);
	    return orderRepository.findByAssignedToAndStatusIn(
	            partner,
	            List.of(
	                Order.OrderStatus.CONFIRMED,
	                Order.OrderStatus.PROCESSING,
	                Order.OrderStatus.SHIPPED
	            ));
	}
	
	public List<Order> getReturnAndExchangeRequests() {
	    return orderRepository.findByStatusIn(List.of(
	            Order.OrderStatus.RETURN_REQUESTED,
	            Order.OrderStatus.EXCHANGE_REQUESTED
	    ));
	}
	
	// admin assigns an order to a delivery partner
	@Transactional
	public Order assignToDeliveryPartner(Long orderId, Long deliveryUserId) {
	    Order order = findById(orderId);
	    User partner = userService.findById(deliveryUserId);
	    order.setAssignedTo(partner);
	    order.setDeliveryPartnerName(partner.getFullName());
	    return orderRepository.save(order);
	}

	public OrderRepository getOrderRepository() {
		return orderRepository;
	}

	public void setOrderRepository(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public CartService getCartService() {
		return cartService;
	}

	public void setCartService(CartService cartService) {
		this.cartService = cartService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Transactional
	public Order updateStatus(Long id, Order.OrderStatus status) {
		Order order = findById(id);
		order.setStatus(status);
		return orderRepository.save(order);
	}
	
	// ✅ NEW — customer submits return or exchange request
	@Transactional
	public void requestReturnOrExchange(Long orderId, String email,
	        String type, String reason) {

	    Order order = findById(orderId);

	    // Only owner can request
	    if (!order.getUser().getEmail().equals(email)) {
	        throw new RuntimeException("Unauthorized action");
	    }

	    // Only allowed when SHIPPED or DELIVERED
	    if (order.getStatus() != Order.OrderStatus.SHIPPED &&
	        order.getStatus() != Order.OrderStatus.DELIVERED) {
	        throw new RuntimeException(
	                "Return/Exchange only allowed after dispatch");
	    }

	    // Set status based on type
	    if ("RETURN".equals(type)) {
	        order.setStatus(Order.OrderStatus.RETURN_REQUESTED);
	    } else if ("EXCHANGE".equals(type)) {
	        order.setStatus(Order.OrderStatus.EXCHANGE_REQUESTED);
	    }

	    order.setReturnType(type);
	    order.setReturnReason(reason);
	    orderRepository.save(order);
	}

	public long getTotalOrders() {
		return orderRepository.count();
	}

	public long getPendingOrders() {
		return orderRepository.countByStatus(Order.OrderStatus.PENDING);
	}

	private String generateOrderNumber() {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		return "SW-" + timestamp + "-" + (int) (Math.random() * 1000);
	}

	private String calculateEstimatedDelivery(String deliverySlot) {
		LocalDate orderDate = LocalDate.now();
		LocalDate deliveryDate;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");

		if (deliverySlot != null && deliverySlot.toLowerCase().contains("weekend")) {
			// Find next Saturday
			deliveryDate = orderDate;
			while (deliveryDate.getDayOfWeek() != DayOfWeek.SATURDAY) {
				deliveryDate = deliveryDate.plusDays(1);
			}
			// If today is already Saturday, take next Saturday
			if (deliveryDate.equals(orderDate)) {
				deliveryDate = deliveryDate.plusDays(7);
			}
		} else {
			// Standard delivery: 3-5 business days
			// Skip weekends while counting
			int daysToAdd = 4; // average 4 business days
			deliveryDate = orderDate;
			int added = 0;
			while (added < daysToAdd) {
				deliveryDate = deliveryDate.plusDays(1);
				if (deliveryDate.getDayOfWeek() != DayOfWeek.SATURDAY
						&& deliveryDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
					added++;
				}
			}
		}
		return deliveryDate.format(formatter);
	}

	public long countByStatus(Order.OrderStatus status) {
	    return orderRepository.countByStatus(status);
	}

}