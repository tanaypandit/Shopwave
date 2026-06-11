package com.ecommerce.repository;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndProduct(User user, Product product);
    // ✅ NEW — find by user + product + size
    Optional<CartItem> findByUserAndProductAndSelectedSize(
            User user, Product product, String selectedSize);
    void deleteByUser(User user);
    int countByUser(User user);
}