package com.ecommerce.controller;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.service.CartService;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
public class CheckoutController {

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	// =============================================
	// STEP 1: Show checkout page (shipping form page)
	// =============================================
	@GetMapping("/checkout")
	public String checkoutPage(Model model, Principal principal) {
		List<CartItem> items = cartService.getCartItems(principal.getName());
		if (items.isEmpty())
			return "redirect:/cart";

		BigDecimal total = cartService.getCartTotal(items);
		BigDecimal shipping = total.compareTo(new BigDecimal("500")) >= 0 ? BigDecimal.ZERO : new BigDecimal("49.00");

		model.addAttribute("cartItems", items);
		model.addAttribute("cartTotal", total);
		model.addAttribute("shippingCost", shipping);
		model.addAttribute("grandTotal", total.add(shipping));
		model.addAttribute("user", userService.findByEmail(principal.getName()));
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("cartCount", items.size());

		return "checkout/checkout";
	}

	// =============================================
	// STEP 2: Move from checkout form -> payment page
	// =============================================
	@PostMapping("/checkout/payment")
	public String paymentPage(@RequestParam String shippingName, @RequestParam String shippingAddress,
			@RequestParam String shippingCity, @RequestParam String shippingState, @RequestParam String shippingZip,
			@RequestParam String shippingPhone, @RequestParam(required = false) String deliverySlot, // ✅ NEW
			Model model, Principal principal, RedirectAttributes redirectAttributes) {

		try {
			List<CartItem> items = cartService.getCartItems(principal.getName());
			if (items.isEmpty())
				return "redirect:/cart";

			BigDecimal total = cartService.getCartTotal(items);
			BigDecimal shipping = total.compareTo(new BigDecimal("500")) >= 0 ? BigDecimal.ZERO
					: new BigDecimal("49.00");

			model.addAttribute("cartItems", items);
			model.addAttribute("cartTotal", total);
			model.addAttribute("shippingCost", shipping);
			model.addAttribute("grandTotal", total.add(shipping));
			model.addAttribute("user", userService.findByEmail(principal.getName()));
			model.addAttribute("categories", categoryService.getAllCategories());
			model.addAttribute("cartCount", items.size());

			model.addAttribute("shippingName", shippingName);
			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("shippingCity", shippingCity);
			model.addAttribute("shippingState", shippingState);
			model.addAttribute("shippingZip", shippingZip);
			model.addAttribute("shippingPhone", shippingPhone);
			model.addAttribute("deliverySlot", deliverySlot); // ✅ NEW

			return "checkout/payment";

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/checkout";
		}
	}

	// =============================================
	// STEP 3: Final order placement after payment
	// =============================================
	@PostMapping("/checkout/place-order")
	public String placeOrder(@RequestParam String shippingName, @RequestParam String shippingAddress,
			@RequestParam String shippingCity, @RequestParam String shippingState, @RequestParam String shippingZip,
			@RequestParam String shippingPhone, @RequestParam String paymentMethod,
			@RequestParam(required = false) String deliverySlot, // ✅ NEW
			Principal principal, RedirectAttributes redirectAttributes) {

		try {
			Order order = orderService.placeOrder(principal.getName(), shippingName, shippingAddress, shippingCity,
					shippingState, shippingZip, shippingPhone, paymentMethod, deliverySlot // ✅ NEW
			);
			return "redirect:/orders/" + order.getId() + "?success=true";

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Order failed: " + e.getMessage());
			return "redirect:/checkout";
		}
	}
	
	// ✅ NEW — customer requests return or exchange
	@PostMapping("/orders/{id}/return-exchange")
	public String requestReturnExchange(
	        @PathVariable Long id,
	        @RequestParam String type,
	        @RequestParam(required = false) String reason,
	        Principal principal,
	        RedirectAttributes ra) {

	    try {
	        orderService.requestReturnOrExchange(
	                id, principal.getName(), type, reason);

	        String msg = "RETURN".equals(type)
	                ? "Return request submitted successfully. Our team will contact you within 24 hours."
	                : "Exchange request submitted successfully. Our team will contact you within 24 hours.";

	        ra.addFlashAttribute("success", msg);

	    } catch (Exception e) {
	        ra.addFlashAttribute("error", e.getMessage());
	    }

	    return "redirect:/orders/" + id;
	}

	@GetMapping("/orders")
	public String myOrders(Model model, Principal principal) {
		model.addAttribute("orders", orderService.getOrdersByUser(principal.getName()));
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("cartCount", cartService.getCartCount(principal.getName()));
		return "orders/list";
	}

	@GetMapping("/orders/{id}")
	public String orderDetail(@PathVariable Long id, Model model, Principal principal) {
		Order order = orderService.findById(id);
		model.addAttribute("order", order);
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("cartCount", cartService.getCartCount(principal.getName()));
		return "orders/detail";
	}

	@GetMapping("/track")
	public String trackOrderPage(Model model) {
		model.addAttribute("categories", categoryService.getAllCategories());
		return "orders/track";
	}

	@PostMapping("/track")
	public String trackOrder(@RequestParam String orderNumber, Model model) {
		try {
			Order order = orderService.findByOrderNumber(orderNumber.trim());
			model.addAttribute("order", order);
		} catch (Exception e) {
			model.addAttribute("error", "No order found with number: " + orderNumber);
		}
		model.addAttribute("orderNumber", orderNumber);
		model.addAttribute("categories", categoryService.getAllCategories());
		return "orders/track";
	}

	@GetMapping("/orders/{id}/bill")
	public String printBill(@PathVariable Long id, Model model, Principal principal) {
		Order order = orderService.findById(id);
		model.addAttribute("order", order);
		return "orders/bill";
	}
	
	@PostMapping("/orders/{id}/cancel")
	public String cancelOrder(@PathVariable Long id,
	                         @RequestParam(required = false) String reason,
	                         Principal principal,
	                         RedirectAttributes ra) {

	    orderService.cancelOrder(id, principal.getName(), reason);
	    ra.addFlashAttribute("success", "Order cancelled successfully");

	    return "redirect:/orders";
	}
}