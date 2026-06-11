package com.ecommerce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    private String selectedSize;

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    // ✅ NEW getter/setter
    public String getSelectedSize() { return selectedSize; }
    public void setSelectedSize(String selectedSize) { this.selectedSize = selectedSize; }

    // ===== Builder =====
    public static OrderItemBuilder builder() { return new OrderItemBuilder(); }

    public static class OrderItemBuilder {
        private Order order;
        private Product product;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
        private String selectedSize; 

        public OrderItemBuilder order(Order order) { this.order = order; return this; }
        public OrderItemBuilder product(Product product) { this.product = product; return this; }
        public OrderItemBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public OrderItemBuilder unitPrice(BigDecimal price) { this.unitPrice = price; return this; }
        public OrderItemBuilder subtotal(BigDecimal subtotal) { this.subtotal = subtotal; return this; }
        public OrderItemBuilder selectedSize(String size) { this.selectedSize = size; return this; } 

        public OrderItem build() {
            OrderItem o = new OrderItem();
            o.order = this.order;
            o.product = this.product;
            o.quantity = this.quantity;
            o.unitPrice = this.unitPrice;
            o.subtotal = this.subtotal;
            o.selectedSize = this.selectedSize; 
            return o;
        }
    }
}