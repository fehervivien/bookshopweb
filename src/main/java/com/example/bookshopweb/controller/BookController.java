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
import org.springframework.web.bind.annotation.ModelAttribute; // Szükséges az @ModelAttribute-hez

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

    // Könyv létrehozása (ez az űrlap beküldéséből jön)
    // Ha van egy űrlap a /books/new vagy /books/add címen, akkor onnan jön a POST
    @PostMapping("/add") // Vagy simán "@PostMapping" ha a gyökér URL-re jön
    public String createBook(@ModelAttribute Book book) { // @ModelAttribute is jó ide
        bookService.saveBook(book);
        return "redirect:/books/list"; // Javított átirányítás
    }

    // Új könyv hozzáadás űrlap megjelenítése (ha van ilyen)
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book"; // feltételezi, hogy van egy add-book.html sablonod
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
            return "redirect:/books/list"; // Javított átirányítás
        }
    }

    // VADONATÚJ METÓDUS: Könyv szerkesztési űrlapjának megjelenítése
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            model.addAttribute("book", book);
            return "edit-book"; // Ez a sablon fájl fog kelleni
        } else {
            // Ha nem található a könyv az ID alapján, visszaküldjük a listára
            return "redirect:/books/list";
        }
    }


    // Könyv frissítése az űrlap beküldése után
    @PutMapping("/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book) {
        // Fontos, hogy az @ModelAttribute Book-ra érkező objektum ID-je megegyezzen az URL-ből jövő ID-vel.
        // A JPA save() metódusa automatikusan frissít, ha az entitásnak van már ID-je.
        book.setId(id);
        bookService.saveBook(book); // Feltételezve, hogy a saveBook a frissítést is kezeli
        return "redirect:/books/list";
    }


    // Könyv törlése
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books/list";
    }
}