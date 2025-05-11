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

//Ez az osztály vezérlőként működik a Spring MVC keretrendszerben.
@Controller
//Az összes vásárlóval kapcsolatos kérést a "/customers" URL-re irányítja.
@RequestMapping("/customers")

public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // -----------------------
    // Vásárló létrehozása
    // -----------------------
    @PostMapping
    public String createCustomer(Customer customer) {
        // vásárló mentése
        customerService.saveCustomer(customer);
        // Visszairányítás a vásárlók listájához
        return "redirect:/customers";
    }

    // -----------------------
    // Összes vásárló lekérdezése
    // -----------------------
    @GetMapping
    public String getAllCustomers(Model model) {
        // vásárlók lekérdezése
        List<Customer> customers = customerService.getAllCustomers();
        // Vásárlók listájának hozzáadása a modelhez
        model.addAttribute("customers", customers);
        // Visszatérés a "customer-list" nevű Thymeleaf sablonhoz
        return "customer-list";
    }

    // -----------------------
    // Vásárló lekérdezése ID alapján
    // -----------------------
    @GetMapping("/{id}")
    public String getCustomerById(@PathVariable Long id, Model model) {
        // vásárló lekérdezése
        Customer customer = customerService.getCustomerById(id).orElse(null);
        if (customer != null) {
            model.addAttribute("customer", customer);
            // Visszatérés a "customer-detail" sablonhoz
            return "customer-detail";
        } else {
            // Ha nem található, visszairányítjuk a vásárlók listájához
            return "redirect:/customers";
        }
    }

    // -----------------------
    // Vásárló törlése
    // -----------------------
    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        // törlés
        customerService.deleteCustomer(id);
        // Törlés után visszairányítjuk a vásárlók listájához
        return "redirect:/customers";
    }
}
