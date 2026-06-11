package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

	@Autowired
	private UserService userService;

	// GET /register lives here only
	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("user", new User());
		return "auth/register";
	}

	// POST /register lives here only
	@PostMapping("/register")
	public String register(@ModelAttribute("user") User user, Model model, RedirectAttributes redirectAttributes) {
		try {

			// 🚨 SECURITY FIX (VERY IMPORTANT)
			if (user.getRole() == User.Role.ADMIN) {
				user.setRole(User.Role.CUSTOMER);
			}

			// fallback safety
			if (user.getRole() == null) {
				user.setRole(User.Role.CUSTOMER);
			}

			userService.register(user);

			redirectAttributes.addFlashAttribute("success", "Account created! Please login.");
			return "redirect:/login";

		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			return "auth/register";
		}
	}
}