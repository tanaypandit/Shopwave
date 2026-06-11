package com.ecommerce.controller;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.service.CartService;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;

@Controller
public class ShopController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @GetMapping("/shop")
    public String shop(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "newest") String sort,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            Model model, Principal principal) {

        Page<Product> products;
        String pageTitle = "All Products";

        if (keyword != null && !keyword.isBlank()) {
            products = productService.searchProducts(
                    keyword, page, size);
            pageTitle = "Search: " + keyword;
            model.addAttribute("keyword", keyword);
        } else if (categoryId != null) {
            Category category = categoryService.findById(categoryId);
            products = productService.getProductsByCategory(
                    category, page, size);
            pageTitle = category.getName();
            model.addAttribute("selectedCategory", category);
        } else {
            products = productService.getAllProducts(page, size, sort);
        }

        model.addAttribute("products", products);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("categories",
                categoryService.getAllCategories());
        model.addAttribute("sort", sort);
        model.addAttribute("currentPage", page);

        if (principal != null) {
            model.addAttribute("cartCount",
                    cartService.getCartCount(principal.getName()));
        }
        return "shop/list";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id,
                                Model model,
                                Principal principal) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("relatedProducts",
                productService.getRelatedProducts(product));
        model.addAttribute("categories",
                categoryService.getAllCategories());
        if (principal != null) {
            model.addAttribute("cartCount",
                    cartService.getCartCount(principal.getName()));
        }
        return "shop/detail";
    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword) {
        return "redirect:/shop?keyword=" + keyword;
    }
}