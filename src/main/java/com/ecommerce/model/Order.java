package com.ecommerce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String orderNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<OrderItem> orderItems = new ArrayList<>();

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal totalAmount;

	@Column(precision = 10, scale = 2)
	private BigDecimal shippingCost = BigDecimal.ZERO;

	@Column(precision = 10, scale = 2)
	private BigDecimal discount = BigDecimal.ZERO;

	@Enumerated(EnumType.STRING)
	private OrderStatus status = OrderStatus.PENDING;

	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus = PaymentStatus.PENDING;

	private String paymentMethod;
	private String shippingName;
	private String shippingAddress;
	private String shippingCity;
	private String shippingState;
	private String shippingZip;
	private String shippingPhone;
	private String deliverySlot;
	private String estimatedDeliveryDate;
	private String deliveryNote;
	private String returnReason;
	private String returnType;
	private String deliveryPartnerName;
	private LocalDateTime dispatchedAt;
	private LocalDateTime deliveredAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assigned_to")
	private User assignedTo;

	@Column(updatable = false)
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public enum OrderStatus {
		PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUNDED, RETURN_REQUESTED, EXCHANGE_REQUESTED
	}

	public enum PaymentStatus {
		PENDING, PAID, FAILED, REFUNDED
	}

	// ===== Getters & Setters =====
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(BigDecimal shippingCost) {
		this.shippingCost = shippingCost;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getShippingCity() {
		return shippingCity;
	}

	public void setShippingCity(String shippingCity) {
		this.shippingCity = shippingCity;
	}

	public String getShippingState() {
		return shippingState;
	}

	public void setShippingState(String shippingState) {
		this.shippingState = shippingState;
	}

	public String getShippingZip() {
		return shippingZip;
	}

	public void setShippingZip(String shippingZip) {
		this.shippingZip = shippingZip;
	}

	public String getShippingPhone() {
		return shippingPhone;
	}

	public void setShippingPhone(String shippingPhone) {
		this.shippingPhone = shippingPhone;
	}

	public String getDeliverySlot() {
		return deliverySlot;
	}

	public void setDeliverySlot(String deliverySlot) {
		this.deliverySlot = deliverySlot;
	}

	public String getDeliveryNote() {
		return deliveryNote;
	}

	public void setDeliveryNote(String deliveryNote) {
		this.deliveryNote = deliveryNote;
	}

	public String getDeliveryPartnerName() {
		return deliveryPartnerName;
	}

	public void setDeliveryPartnerName(String deliveryPartnerName) {
		this.deliveryPartnerName = deliveryPartnerName;
	}

	public User getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}

	public LocalDateTime getDispatchedAt() {
		return dispatchedAt;
	}

	public void setDispatchedAt(LocalDateTime dispatchedAt) {
		this.dispatchedAt = dispatchedAt;
	}

	public LocalDateTime getDeliveredAt() {
		return deliveredAt;
	}

	public void setDeliveredAt(LocalDateTime deliveredAt) {
		this.deliveredAt = deliveredAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getEstimatedDeliveryDate() {
		return estimatedDeliveryDate;
	}

	public void setEstimatedDeliveryDate(String estimatedDeliveryDate) {
		this.estimatedDeliveryDate = estimatedDeliveryDate;
	}

	// ===== Builder =====
	public static OrderBuilder builder() {
		return new OrderBuilder();
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public static class OrderBuilder {
		private String orderNumber;
		private User user;
		private List<OrderItem> orderItems = new ArrayList<>();
		private BigDecimal totalAmount;
		private BigDecimal shippingCost = BigDecimal.ZERO;
		private BigDecimal discount = BigDecimal.ZERO;
		private OrderStatus status = OrderStatus.PENDING;
		private PaymentStatus paymentStatus = PaymentStatus.PENDING;
		private String paymentMethod;
		private String shippingName;
		private String shippingAddress;
		private String shippingCity;
		private String shippingState;
		private String shippingZip;
		private String shippingPhone;
		private String deliverySlot;
		private String estimatedDeliveryDate;

		public OrderBuilder orderNumber(String n) {
			this.orderNumber = n;
			return this;
		}

		public OrderBuilder user(User u) {
			this.user = u;
			return this;
		}

		public OrderBuilder orderItems(List<OrderItem> items) {
			this.orderItems = items;
			return this;
		}

		public OrderBuilder totalAmount(BigDecimal a) {
			this.totalAmount = a;
			return this;
		}

		public OrderBuilder shippingCost(BigDecimal s) {
			this.shippingCost = s;
			return this;
		}

		public OrderBuilder discount(BigDecimal d) {
			this.discount = d;
			return this;
		}

		public OrderBuilder status(OrderStatus s) {
			this.status = s;
			return this;
		}

		public OrderBuilder paymentStatus(PaymentStatus p) {
			this.paymentStatus = p;
			return this;
		}

		public OrderBuilder paymentMethod(String m) {
			this.paymentMethod = m;
			return this;
		}

		public OrderBuilder shippingName(String n) {
			this.shippingName = n;
			return this;
		}

		public OrderBuilder shippingAddress(String a) {
			this.shippingAddress = a;
			return this;
		}

		public OrderBuilder shippingCity(String c) {
			this.shippingCity = c;
			return this;
		}

		public OrderBuilder shippingState(String s) {
			this.shippingState = s;
			return this;
		}

		public OrderBuilder shippingZip(String z) {
			this.shippingZip = z;
			return this;
		}

		public OrderBuilder shippingPhone(String p) {
			this.shippingPhone = p;
			return this;
		}

		public OrderBuilder deliverySlot(String ds) {
			this.deliverySlot = ds;
			return this;
		}

		public OrderBuilder estimatedDeliveryDate(String d) {
			this.estimatedDeliveryDate = d;
			return this;
		}

		public Order build() {
			Order o = new Order();
			o.orderNumber = this.orderNumber;
			o.user = this.user;
			o.orderItems = this.orderItems;
			o.totalAmount = this.totalAmount;
			o.shippingCost = this.shippingCost;
			o.discount = this.discount;
			o.status = this.status;
			o.paymentStatus = this.paymentStatus;
			o.paymentMethod = this.paymentMethod;
			o.shippingName = this.shippingName;
			o.shippingAddress = this.shippingAddress;
			o.shippingCity = this.shippingCity;
			o.shippingState = this.shippingState;
			o.shippingZip = this.shippingZip;
			o.shippingPhone = this.shippingPhone;
			o.deliverySlot = this.deliverySlot;
			o.estimatedDeliveryDate = this.estimatedDeliveryDate;
			return o;
		}
	}
}