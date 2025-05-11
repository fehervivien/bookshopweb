package com.example.bookshopweb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.bookshopweb.entity.Book;
import com.example.bookshopweb.service.BookService;

/*
 * A BookController osztály a könyvek kezeléséért felelős
 * a Spring MVC keretrendszerben.
 * Ez az osztály kezeli a könyvekkel kapcsolatos HTTP kéréseket,
 * mint például a könyvek létrehozása, lekérdezése, frissítése és törlése.
 */

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // -----------------------
    // Könyv létrehozása
    // -----------------------
    @PostMapping
    public String createBook(Book book) {
        // könyv mentése
        bookService.saveBook(book);
        // Új könyv után visszairányítjuk az összes könyv listájához
        return "redirect:/books";
    }

    // -----------------------
    // Összes könyv lekérdezése
    // -----------------------
    @GetMapping
    public String getAllBooks(Model model) {
        // könyvek lekérdezése
        List<Book> books = bookService.getAllBooks();
        // Könyvek listájának hozzáadása a modelhez
        model.addAttribute("books", books);
        // Visszatérés a "book-list" nevű Thymeleaf sablonhoz
        return "book-list";
    }

    // -----------------------
    // Egy könyv lekérdezése ID alapján
    // -----------------------
    @GetMapping("/{id}")
    public String getBookById(@PathVariable Long id, Model model) {
        // könyv lekérdezése
        Book book = bookService.getBookById(id);
        if (book != null) {
            model.addAttribute("book", book);
            // Visszatérés a "book-detail" sablonhoz
            return "book-detail";
        } else {
            // Ha nem található, visszairányítjuk az összes könyvhöz
            return "redirect:/books";
        }
    }

    // -----------------------
    // Könyv frissítése
    // -----------------------
    @PutMapping("/{id}")
    public String updateBook(@PathVariable Long id, Book book) {
        // könyv frissítése
        bookService.updateBook(id, book);
        // Könyv frissítése után visszairányítjuk az összes könyv listájához
        return "redirect:/books";
    }

    // -----------------------
    // Könyv törlése
    // -----------------------
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {
        // törlés
        bookService.deleteBook(id);
        // Törlés után visszairányítjuk az összes könyvhöz
        return "redirect:/books";
    }
}
