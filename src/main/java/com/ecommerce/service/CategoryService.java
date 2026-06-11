package com.ecommerce.service;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category findBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Category not found: " + slug));
    }

    @Transactional
    public Category save(Category category) {
        if (category.getName() != null) {
            category.setName(category.getName().trim());
        }
        if (category.getSlug() == null || category.getSlug().isBlank()) {
            category.setSlug(category.getName()
                    .toLowerCase()
                    .replaceAll("\\s+", "-")
                    .trim());
        }
        if (category.getId() == null) {
            boolean exists = categoryRepository.findAll()
                    .stream()
                    .anyMatch(c -> c.getName().trim()
                            .equalsIgnoreCase(category.getName().trim()));
            if (exists) {
                throw new RuntimeException(
                    "Category '" + category.getName() + "' already exists."
                );
            }
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // ✅ Delete all products under this category first
        List<Product> products = productRepository.findByCategory(category);
        productRepository.deleteAll(products);

        // ✅ Then delete the category
        categoryRepository.deleteById(id);
    }
}