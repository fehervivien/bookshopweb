package com.example.bookshopweb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.bookshopweb.entity.Customer;
import com.example.bookshopweb.service.CustomerService;

/*
 * A CustomerController osztály felelős a vásárlók létrehozásáért,
 * lekérdezéséért és törléséért.
 * A Spring MVC keretrendszer segítségével valósítja meg a HTTP kérések kezelését.
 */


@Controller
@RequestMapping("/customers")

public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Vásárló létrehozása
    @PostMapping
    public String createCustomer(Customer customer) {
        customerService.saveCustomer(customer);
        return "redirect:/customers";
    }

    // Összes vásárló lekérdezése
    @GetMapping
    public String getAllCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customer-list";
    }

    // Vásárló lekérdezése ID alapján
    @GetMapping("/{id}")
    public String getCustomerById(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id).orElse(null);
        if (customer != null) {
            model.addAttribute("customer", customer);
            return "customer-detail";
        } else {
            return "redirect:/customers";
        }
    }

    // Vásárló törlése
    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}
