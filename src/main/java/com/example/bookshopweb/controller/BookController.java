package com.example.bookshopweb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping; // Ezt már nem használjuk, de bent maradhat
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; // Ezt fogjuk használni a PUT/DELETE helyett
import org.springframework.web.bind.annotation.PutMapping; // Ezt már nem használjuk, de bent maradhat
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.bookshopweb.entity.Book;
import com.example.bookshopweb.service.BookService;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Új könyv hozzáadás űrlap megjelenítése
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    // Könyv létrehozása (a /books/add űrlapról jövő POST kérés)
    @PostMapping("/add")
    public String createBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return "redirect:/books/list";
    }

    // Összes könyv lekérdezése
    @GetMapping("/list")
    public String getAllBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "book-list";
    }

    // Egy könyv lekérdezése ID alapján (részletes nézet)
    @GetMapping("/{id}")
    public String getBookById(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            model.addAttribute("book", book);
            return "book-detail";
        } else {
            return "redirect:/books/list";
        }
    }

    // Könyv szerkesztési űrlapjának megjelenítése
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            model.addAttribute("book", book);
            return "edit-book";
        } else {
            return "redirect:/books/list";
        }
    }

    // Könyv frissítése (MOST POST metódussal, dedikált URL-el)
    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book) {
        book.setId(id);
        bookService.saveBook(book);
        return "redirect:/books/list";
    }

    // Könyv törlése (MOST POST metódussal, dedikált URL-el)
    // A HtmL-ben is a /books/delete/{id} POST kérésre fog mutatni az űrlap
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books/list";
    }
}