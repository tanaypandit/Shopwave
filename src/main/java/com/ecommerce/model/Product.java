package com.ecommerce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal price;

	@Column(nullable = false)
	private Integer stockQuantity;

	@Column(columnDefinition = "TEXT")
	private String imageUrl;
	private String brand;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	private boolean active = true;

	@Column(precision = 3, scale = 2)
	private BigDecimal rating = BigDecimal.ZERO;

	private Integer reviewCount = 0;
	
	private String availableSizes;

	
	private boolean wearable = false;

	@Transient
	private Long categoryId;

	@Column(updatable = false)
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		setUpdatedAt(LocalDateTime.now());
	}

	@PreUpdate
	protected void onUpdate() {
		setUpdatedAt(LocalDateTime.now());
	}

	public boolean isInStock() {
		return stockQuantity != null && stockQuantity > 0;
	}

	// ===== Getters & Setters =====
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

	public Integer getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	// ===== Builder =====
	public static ProductBuilder builder() {
		return new ProductBuilder();
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public boolean isWearable() {
		return wearable;
	}

	public void setWearable(boolean wearable) {
		this.wearable = wearable;
	}

	public String getAvailableSizes() {
		return availableSizes;
	}

	public void setAvailableSizes(String availableSizes) {
		this.availableSizes = availableSizes;
	}

	public static class ProductBuilder {
		private String name;
		private String description;
		private BigDecimal price;
		private Integer stockQuantity;
		private String imageUrl;
		private String brand;
		private Category category;
		private boolean active = true;
		private BigDecimal rating = BigDecimal.ZERO;
		private Integer reviewCount = 0;
		private boolean hasSize;

		public ProductBuilder name(String name) {
			this.name = name;
			return this;
		}

		public ProductBuilder description(String desc) {
			this.description = desc;
			return this;
		}

		public ProductBuilder price(BigDecimal price) {
			this.price = price;
			return this;
		}

		public ProductBuilder stockQuantity(Integer qty) {
			this.stockQuantity = qty;
			return this;
		}

		public ProductBuilder imageUrl(String url) {
			this.imageUrl = url;
			return this;
		}

		public ProductBuilder brand(String brand) {
			this.brand = brand;
			return this;
		}

		public ProductBuilder category(Category cat) {
			this.category = cat;
			return this;
		}

		public ProductBuilder active(boolean active) {
			this.active = active;
			return this;
		}

		public ProductBuilder rating(BigDecimal rating) {
			this.rating = rating;
			return this;
		}

		public ProductBuilder reviewCount(Integer count) {
			this.reviewCount = count;
			return this;
		}
		
		public boolean isHasSize() {
		    return hasSize;
		}

		public void setHasSize(boolean hasSize) {
		    this.hasSize = hasSize;
		}

		public Product build() {
			Product p = new Product();
			p.name = this.name;
			p.description = this.description;
			p.price = this.price;
			p.stockQuantity = this.stockQuantity;
			p.imageUrl = this.imageUrl;
			p.brand = this.brand;
			p.category = this.category;
			p.active = this.active;
			p.rating = this.rating;
			p.reviewCount = this.reviewCount;
			return p;
		}
	}
}