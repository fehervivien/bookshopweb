package com.example.bookshopweb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.bookshopweb.entity.Order;
import com.example.bookshopweb.service.OrderService;

/*
 * Az OrderController osztály felelős a rendelésekkel kapcsolatos műveletekért,
 * mint például rendelés létrehozása, lekérdezése és törlése.
 * A Spring MVC keretrendszer segítségével valósítja meg
 * a HTTP kérések kezelését.
 */

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Az összes rendelés lekérdezése
    @GetMapping
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "order-list";
    }

    // Egy rendelés lekérdezése ID alapján
    @GetMapping("/{id}")
    public String getOrderById(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id);
        if (order != null) {
            model.addAttribute("order", order);
            return "order-detail";
        } else {
            return "redirect:/orders";
        }
    }

    // Új rendelés létrehozása
    @PostMapping
    public String createOrder(Order order) {
        orderService.save(order);
        return "redirect:/orders";
    }

    // Rendelés frissítése
    @PutMapping("/{id}")
    public String updateOrder(@PathVariable Long id, Order orderDetails) {
        Order existingOrder = orderService.findById(id);
        existingOrder.setBook(orderDetails.getBook());
        existingOrder.setCustomer(orderDetails.getCustomer());
        existingOrder.setOrderDate(orderDetails.getOrderDate());
        orderService.save(existingOrder);
        return "redirect:/orders";
    }

    // Rendelés törlése
    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteById(id);
        return "redirect:/orders";
    }
}
