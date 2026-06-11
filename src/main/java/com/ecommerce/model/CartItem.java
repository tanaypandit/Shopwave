package com.ecommerce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    // ✅ NEW — stores chosen size for wearable products
    private String selectedSize;

    public BigDecimal getSubtotal() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    // ✅ NEW getter/setter
    public String getSelectedSize() { return selectedSize; }
    public void setSelectedSize(String selectedSize) { this.selectedSize = selectedSize; }

    // ===== Builder =====
    public static CartItemBuilder builder() { return new CartItemBuilder(); }

    public static class CartItemBuilder {
        private User user;
        private Product product;
        private Integer quantity;
        private String selectedSize; // ✅ NEW

        public CartItemBuilder user(User user) { this.user = user; return this; }
        public CartItemBuilder product(Product product) { this.product = product; return this; }
        public CartItemBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public CartItemBuilder selectedSize(String size) { this.selectedSize = size; return this; } // ✅ NEW

        public CartItem build() {
            CartItem c = new CartItem();
            c.user = this.user;
            c.product = this.product;
            c.quantity = this.quantity;
            c.selectedSize = this.selectedSize; // ✅ NEW
            return c;
        }
    }
}