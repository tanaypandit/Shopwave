package com.ecommerce.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;
    @Column(columnDefinition = "TEXT")
    private String imageUrl;
    private String slug;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    // ===== Builder =====
    public static CategoryBuilder builder() { return new CategoryBuilder(); }

    public static class CategoryBuilder {
        private String name;
        private String description;
        private String imageUrl;
        private String slug;

        public CategoryBuilder name(String name) { this.name = name; return this; }
        public CategoryBuilder description(String desc) { this.description = desc; return this; }
        public CategoryBuilder imageUrl(String url) { this.imageUrl = url; return this; }
        public CategoryBuilder slug(String slug) { this.slug = slug; return this; }

        public Category build() {
            Category c = new Category();
            c.name = this.name;
            c.description = this.description;
            c.imageUrl = this.imageUrl;
            c.slug = this.slug;
            return c;
        }
    }
}