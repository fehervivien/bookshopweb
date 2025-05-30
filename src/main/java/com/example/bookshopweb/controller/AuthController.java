package com.example.bookshopweb.controller;

import com.example.bookshopweb.entity.User;
import com.example.bookshopweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam; // A login error/logout paraméterekhez

/* AuthController: A regisztrációs és bejelentkezési funkciókhoz
   szükséges vezérlőosztály
   Hozzá tartozó osztályok: UserService, User
*/
@Controller
public class AuthController {

    private final UserService userService;

    // Konstruktor, amely injektálja a UserService-t,
    // ez felelős a felhasználói műveletekért
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Regisztrációs űrlap megjelenítése
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Ez a sablonfájl lesz
    }

    // Regisztráció beküldése
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            userService.registerNewUser(user);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
        return "redirect:/login?registered=true";
    }

    // Bejelentkezési űrlap megjelenítése, és kezelése
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                @RequestParam(value = "registered", required = false) String registered,
                                Model model) {
        // Ha van hiba, logout vagy regisztrációs üzenet,
        if (error != null) {
            model.addAttribute("errorMessage", "Hibás felhasználónév vagy jelszó.");
        }
        if (logout != null) {
            model.addAttribute("message", "Sikeresen kijelentkezett.");
        }
        if (registered != null) {
            model.addAttribute("message", "Sikeres regisztráció! Kérjük, jelentkezzen be.");
        }
        return "login";
    }
}