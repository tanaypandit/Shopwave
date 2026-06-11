package com.ecommerce.service;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getAllProducts(int page, int size, String sort) {
        Sort sortObj = switch (sort) {
            case "price_asc"  -> Sort.by("price").ascending();
            case "price_desc" -> Sort.by("price").descending();
            case "rating"     -> Sort.by("rating").descending();
            case "newest"     -> Sort.by("createdAt").descending();
            default           -> Sort.by("name").ascending();
        };
        return productRepository.findByActiveTrue(
                PageRequest.of(page, size, sortObj));
    }

    public Page<Product> getProductsByCategory(Category category,
                                                int page, int size) {
        return productRepository.findByCategoryAndActiveTrue(category,
                PageRequest.of(page, size, Sort.by("name")));
    }

    public Page<Product> searchProducts(String keyword, int page, int size) {
        return productRepository.searchProducts(keyword,
                PageRequest.of(page, size, Sort.by("name")));
    }

    public Page<Product> filterByPrice(BigDecimal min, BigDecimal max,
                                        int page, int size) {
        return productRepository.findByPriceRange(min, max,
                PageRequest.of(page, size, Sort.by("price")));
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Product not found: " + id));
    }

    public List<Product> getNewArrivals() {
        return productRepository
                .findTop8ByActiveTrueOrderByCreatedAtDesc();
    }

    public List<Product> getTopRated() {
        return productRepository
                .findTop8ByActiveTrueOrderByRatingDesc();
    }

    public List<Product> getRelatedProducts(Product product) {
        return productRepository.findByCategoryAndActiveTrueAndIdNot(
                product.getCategory(), product.getId(),
                PageRequest.of(0, 4));
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public void delete(Long id) {
        Product p = findById(id);
        p.setActive(false);
        productRepository.save(p);
    }

    public long getTotalProducts() {
        return productRepository.count();
    }
}