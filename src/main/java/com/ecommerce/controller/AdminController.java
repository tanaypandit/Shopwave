package com.ecommerce.controller;

import com.ecommerce.model.Category;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	// ---- Dashboard ----
	@GetMapping
	public String dashboard(Model model) {
		model.addAttribute("totalProducts", productService.getTotalProducts());
		model.addAttribute("totalOrders", orderService.getTotalOrders());
		model.addAttribute("totalUsers", userService.getTotalUsers());
		model.addAttribute("pendingOrders", orderService.getPendingOrders());
		model.addAttribute("recentOrders", orderService.getAllOrders(0, 5).getContent());
		// ✅ NEW — count of return/exchange requests
		model.addAttribute("returnRequests", orderService.countByStatus(Order.OrderStatus.RETURN_REQUESTED));
		model.addAttribute("exchangeRequests", orderService.countByStatus(Order.OrderStatus.EXCHANGE_REQUESTED));
		return "admin/dashboard";
	}

	// ---- Products ----
	@GetMapping("/products")
	public String products(@RequestParam(defaultValue = "0") int page, Model model) {
		model.addAttribute("products", productService.getAllProducts(page, 10, "newest"));
		model.addAttribute("categories", categoryService.getAllCategories());
		return "admin/products";
	}

	@GetMapping("/products/new")
	public String newProductForm(Model model) {
		model.addAttribute("product", new Product());
		model.addAttribute("categories", categoryService.getAllCategories());
		return "admin/product-form";
	}

	@GetMapping("/products/edit/{id}")
	public String editProductForm(@PathVariable Long id, Model model) {
		model.addAttribute("product", productService.findById(id));
		model.addAttribute("categories", categoryService.getAllCategories());
		return "admin/product-form";
	}

	@PostMapping("/products/save")
	public String saveProduct(@ModelAttribute Product product, RedirectAttributes ra) {
		if (product.getCategoryId() != null) {
			product.setCategory(categoryService.findById(product.getCategoryId()));
		}
		productService.save(product);
		ra.addFlashAttribute("success", "Product saved successfully!");
		return "redirect:/admin/products";
	}

	@PostMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable Long id, RedirectAttributes ra) {
		productService.delete(id);
		ra.addFlashAttribute("success", "Product deleted.");
		return "redirect:/admin/products";
	}

	// ---- Categories ----
	@GetMapping("/categories")
	public String categories(Model model) {
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("category", new Category());
		return "admin/categories";
	}

	@PostMapping("/categories/save")
	public String saveCategory(@ModelAttribute Category category, RedirectAttributes ra) {
		try {
			categoryService.save(category);
			ra.addFlashAttribute("success", "Category saved!");
		} catch (Exception e) {
			ra.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/admin/categories";
	}

	@PostMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable Long id, RedirectAttributes ra) {
	    try {
	        categoryService.delete(id);
	        ra.addFlashAttribute("success", "Category deleted.");
	    } catch (Exception e) {
	        ra.addFlashAttribute("error", e.getMessage());
	    }
	    return "redirect:/admin/categories";
	}

	// ---- Orders ----
	@GetMapping("/orders")
	public String orders(@RequestParam(defaultValue = "0") int page, Model model) {
		Page<Order> orders = orderService.getAllOrders(page, 10);

		model.addAttribute("orders", orders);

		model.addAttribute("statuses", Order.OrderStatus.values());

		return "admin/orders";
	}

	@PostMapping("/orders/{id}/status")
	public String updateOrderStatus(@PathVariable Long id, @RequestParam Order.OrderStatus status,
			RedirectAttributes ra) {
		orderService.updateStatus(id, status);
		ra.addFlashAttribute("success", "Order status updated.");
		return "redirect:/admin/orders";
	}

	// show assign delivery partner page
	@GetMapping("/orders/{id}/assign")
	public String assignDeliveryPage(@PathVariable Long id, Model model) {
		model.addAttribute("order", orderService.findById(id));
		model.addAttribute("deliveryPartners", userService.getAllDeliveryPartners());
		return "admin/assign-delivery";
	}

	// save the assignment
	@PostMapping("/orders/{id}/assign")
	public String assignDelivery(@PathVariable Long id, @RequestParam Long deliveryUserId, RedirectAttributes ra) {
		orderService.assignToDeliveryPartner(id, deliveryUserId);
		ra.addFlashAttribute("success", "Order assigned to delivery partner.");
		return "redirect:/admin/orders";
	}

	// ✅ NEW — Admin view order detail
	@GetMapping("/orders/{id}")
	public String orderDetail(@PathVariable Long id, Model model) {
		model.addAttribute("order", orderService.findById(id));
		return "admin/order-detail";
	}
}