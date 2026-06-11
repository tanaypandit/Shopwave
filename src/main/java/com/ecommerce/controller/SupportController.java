package com.ecommerce.controller;

import com.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SupportController {

    @Autowired
    private CategoryService categoryService;

    private void addCommon(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
    }

    @GetMapping("/faq")
    public String faq(Model model) {
        addCommon(model);
        return "support/faq";
    }

    @GetMapping("/shipping-policy")
    public String shippingPolicy(Model model) {
        addCommon(model);
        return "support/shippingpolicy";   // FIXED
    }

    @GetMapping("/returns")
    public String returns(Model model) {
        addCommon(model);
        return "support/returns";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        addCommon(model);
        return "support/contact";
    }

    @PostMapping("/contact")
    public String submitContact(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String subject,
            @RequestParam String message,
            Model model) {

        model.addAttribute("success",
                "Thank you " + name + "! We'll get back to you within 24 hours.");
        addCommon(model);
        return "support/contact";
    }
}