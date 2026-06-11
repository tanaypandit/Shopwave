package com.ecommerce.service;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    public List<CartItem> getCartItems(String email) {
        User user = userService.findByEmail(email);
        return cartItemRepository.findByUser(user);
    }

    public int getCartCount(String email) {
        User user = userService.findByEmail(email);
        return cartItemRepository.countByUser(user);
    }

    @Transactional
    public void addToCart(String email, Long productId,
                          int quantity, String selectedSize) { // ✅ NEW param
        User user = userService.findByEmail(email);
        Product product = productService.findById(productId);
       
        Optional<CartItem> existing;
        if (selectedSize != null && !selectedSize.isBlank()) {
            existing = cartItemRepository
                    .findByUserAndProductAndSelectedSize(
                            user, product, selectedSize);
        } else {
            existing = cartItemRepository
                    .findByUserAndProduct(user, product);
        }

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem item = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(quantity)
                    .selectedSize(selectedSize) // ✅ NEW
                    .build();
            cartItemRepository.save(item);
        }
    }

    @Transactional
    public void updateQuantity(String email, Long itemId, int quantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!item.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }
        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    @Transactional
    public void removeItem(String email, Long itemId) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!item.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }
        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(String email) {
        User user = userService.findByEmail(email);
        cartItemRepository.deleteByUser(user);
    }

    public BigDecimal getCartTotal(List<CartItem> items) {
        return items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}