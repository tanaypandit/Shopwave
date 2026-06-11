package com.ecommerce.controller;

import com.ecommerce.model.CartItem;
import com.ecommerce.service.CartService;
import com.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping
	public String viewCart(Model model, Principal principal) {
		List<CartItem> items = cartService.getCartItems(principal.getName());
		BigDecimal total = cartService.getCartTotal(items);
		BigDecimal shipping = total.compareTo(new BigDecimal("500")) >= 0 ? BigDecimal.ZERO : new BigDecimal("49.00");

		model.addAttribute("cartItems", items);
		model.addAttribute("cartTotal", total);
		model.addAttribute("shippingCost", shipping);
		model.addAttribute("grandTotal", total.add(shipping));
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("cartCount", items.size());
		return "cart/view";
	}

	@PostMapping("/add")
	public String addToCart(@RequestParam Long productId, @RequestParam(defaultValue = "1") int quantity,
			@RequestParam(required = false) String selectedSize, Principal principal,
			RedirectAttributes redirectAttributes) {

		// Only block if selectedSize was submitted but no value chosen
		// Non-fashion products send nothing — that is fine
		// Fashion products send "SELECT_SIZE" placeholder if nothing picked
		if (selectedSize != null && selectedSize.equals("SELECT_SIZE")) {
			redirectAttributes.addFlashAttribute("error", "Please select a size before adding to cart.");
			return "redirect:/product/" + productId;
		}

		// Treat empty string same as null
		String sizeToSave = (selectedSize == null || selectedSize.isBlank()) ? null : selectedSize;

		cartService.addToCart(principal.getName(), productId, quantity, sizeToSave);
		redirectAttributes.addFlashAttribute("success", "Item added to cart!");
		return "redirect:/cart";
	}

	@PostMapping("/update/{itemId}")
	public String updateQuantity(@PathVariable Long itemId, @RequestParam int quantity, Principal principal) {
		cartService.updateQuantity(principal.getName(), itemId, quantity);
		return "redirect:/cart";
	}

	@PostMapping("/remove/{itemId}")
	public String removeItem(@PathVariable Long itemId, Principal principal) {
		cartService.removeItem(principal.getName(), itemId);
		return "redirect:/cart";
	}
}