package com.ecommerce.config;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) {

		boolean usersExist = userRepository.count() > 0;
		boolean categoriesExist = categoryRepository.count() > 0;
		boolean productsExist = productRepository.count() > 0;

		if (usersExist && categoriesExist && productsExist) {
			log.info("Database already seeded. Skipping.");
			return;
		}

		log.info("Seeding database with sample data...");

		if (!usersExist) {
			seedUsers();
		} else {
			log.info("Users already exist. Skipping user seed.");
		}

		if (!categoriesExist && !productsExist) {
			seedCategories();
		} else {
			log.info("Categories/Products already exist. Skipping.");
		}

		log.info("Database seeding complete.");
	}

	private void seedUsers() {
		User admin = User.builder().fullName("Admin User").email("admin@shopwave.com")
				.password(passwordEncoder.encode("admin123")).phone("9999999999").role(User.Role.ADMIN).enabled(true)
				.build();
		userRepository.save(admin);

		User customer = User.builder().fullName("Customer").email("customer@shopwave.com")
				.password(passwordEncoder.encode("customer123")).phone("8888888888").role(User.Role.CUSTOMER)
				.enabled(true).build();
		userRepository.save(customer);

		User delivery = User.builder().fullName("Delivery Partner").email("delivery@shopwave.com")
				.password(passwordEncoder.encode("delivery123")).phone("7777777777").role(User.Role.DELIVERY)
				.enabled(true).build();
		userRepository.save(delivery);

		log.info("Users seeded: admin@shopwave.com / admin123 "
							+ "customer@shopwave.com / customer123 "
							+ "delivery@shopwave.com / delivery123");

	}

	private void seedCategories() {
		Category electronics = saveCategory("Electronics", "electronics", "Latest gadgets and devices",
				"https://images.unsplash.com/photo-1498049794561-7780e7231661?w=400");
		Category fashion = saveCategory("Fashion", "fashion", "Trendy clothing and accessories",
				"https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=400");
		Category home = saveCategory("Home & Living", "home-living", "Furniture and decor",
				"https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=400");
		Category sports = saveCategory("Sports", "sports", "Sports and fitness equipment",
				"https://images.unsplash.com/photo-1461896836934-ffe607ba8211?w=400");
		Category books = saveCategory("Books", "books", "Books, education and stationery",
				"https://images.unsplash.com/photo-1495446815901-a7297e633e8d?w=400");

		seedProducts(electronics, fashion, home, sports, books);
	}

	private Category saveCategory(String name, String slug, String desc, String img) {

		return categoryRepository.findByName(name).orElseGet(() -> categoryRepository
				.save(Category.builder().name(name).slug(slug).description(desc).imageUrl(img).build()));
	}

	private void seedProducts(Category electronics, Category fashion, Category home, Category sports, Category books) {
		saveProduct("Wireless Noise-Cancelling Headphones", electronics,
				"Premium audio with 30hr battery and active noise cancellation.", new BigDecimal("2999.00"), 50,
				"SoundPro", "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400",
				new BigDecimal("4.5"));
		saveProduct("Smart Watch Pro", electronics, "Health tracking, GPS, AMOLED display with 7-day battery.",
				new BigDecimal("4999.00"), 30, "TechWear",
				"https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400", new BigDecimal("4.3"));
		saveProduct("Mechanical Keyboard RGB", electronics, "Blue switches, per-key RGB, USB-C connectivity.",
				new BigDecimal("1499.00"), 75, "KeyMaster",
				"https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=400", new BigDecimal("4.6"));
		saveProduct("4K Webcam", electronics, "Ultra HD video, built-in ring light, auto-focus.",
				new BigDecimal("2499.00"), 40, "VisionCam",
				"https://images.unsplash.com/photo-1596742578443-7682ef5251cd?w=400", new BigDecimal("4.2"));
		saveProduct("Classic Denim Jacket", fashion, "100% cotton, vintage wash, multiple pockets.",
				new BigDecimal("1299.00"), 60, "UrbanThread",
				"https://images.unsplash.com/photo-1551537482-f2075a1d41f2?w=400", new BigDecimal("4.4"));
		saveProduct("Slim Fit Chinos", fashion, "Stretch fabric, tapered leg, available in 6 colors.",
				new BigDecimal("799.00"), 100, "StyleCo",
				"https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?w=400", new BigDecimal("4.1"));
		saveProduct("Leather Sneakers", fashion, "Genuine leather upper, rubber sole, classic design.",
				new BigDecimal("1899.00"), 45, "StepUp",
				"https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400", new BigDecimal("4.7"));
		saveProduct("Ergonomic Office Chair", home, "Lumbar support, adjustable armrests, mesh back.",
				new BigDecimal("8999.00"), 20, "ComfortSeats",
				"https://images.unsplash.com/photo-1592078615290-033ee584e267?w=400", new BigDecimal("4.5"));
		saveProduct("Ceramic Dinner Set (12 pcs)", home, "Dishwasher-safe, chip-resistant, elegant matte finish.",
				new BigDecimal("1599.00"), 35, "TableArt",
				"https://images.unsplash.com/photo-1603199506016-b9a594b593c0?w=400", new BigDecimal("4.3"));
		saveProduct("Scented Soy Candle Set", home, "Set of 4, long-lasting, handcrafted, various fragrances.",
				new BigDecimal("599.00"), 80, "LumeCraft",
				"https://images.unsplash.com/photo-1603906210009-2f4bd3f1c0e5?w=400", new BigDecimal("4.8"));
		saveProduct("Yoga Mat Premium", sports, "Non-slip, eco-friendly TPE, 6mm thickness, carry strap.",
				new BigDecimal("899.00"), 90, "ZenFit",
				"https://images.unsplash.com/photo-1599901860904-17e6ed7083a0?w=400", new BigDecimal("4.6"));
		saveProduct("Adjustable Dumbbell Set", sports, "5-25kg adjustable, space-saving, quick-change dial.",
				new BigDecimal("3499.00"), 25, "IronEdge",
				"https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=400", new BigDecimal("4.4"));
		saveProduct("Atomic Habits", books, "James Clear's bestseller on building good habits.",
				new BigDecimal("399.00"), 200, "Penguin",
				"https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=400", new BigDecimal("4.9"));
		saveProduct("The Art of War", books, "Sun Tzu's ancient masterpiece on strategy.", new BigDecimal("249.00"),
				150, "Oxford", "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=400",
				new BigDecimal("4.7"));
	}

	private void saveProduct(String name, Category category, String desc, BigDecimal price, int stock, String brand,
			String imageUrl, BigDecimal rating) {
		productRepository.save(Product.builder().name(name).category(category).description(desc).price(price)
				.stockQuantity(stock).brand(brand).imageUrl(imageUrl).rating(rating)
				.reviewCount((int) (Math.random() * 200 + 10)).active(true).build());
	}
}