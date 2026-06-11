package com.ecommerce.controller;

import com.ecommerce.service.CartService;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        model.addAttribute("categories",
                categoryService.getAllCategories());
        model.addAttribute("newArrivals",
                productService.getNewArrivals());
        model.addAttribute("topRated",
                productService.getTopRated());
        if (principal != null) {
            model.addAttribute("cartCount",
                    cartService.getCartCount(principal.getName()));
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    // ✅ REMOVED @GetMapping("/register") — it lives in AuthController only
}