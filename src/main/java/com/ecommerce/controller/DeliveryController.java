package com.ecommerce.controller;

import com.ecommerce.model.Order;
import com.ecommerce.service.OrderService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/login")
    public String deliveryLogin() {
        return "delivery/login";
    }

    // ✅ NOW shows only orders assigned to the logged-in delivery partner
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("orders",
                orderService.getOrdersForDeliveryPartner(
                        principal.getName()));
        return "delivery/dashboard";
    }
    
    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id,
            Model model, Principal principal) {
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        return "delivery/order-detail";
    }

    @PostMapping("/orders/{id}/update")
    public String updateStatus(@PathVariable Long id,
            @RequestParam Order.OrderStatus status,
            @RequestParam String deliveryNote,
            RedirectAttributes ra) {
        orderService.updateStatusByDelivery(id, status, deliveryNote);
        ra.addFlashAttribute("success",
                "Order status updated to: " + status);
        return "redirect:/delivery/dashboard";
    }
}