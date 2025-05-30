package com.example.bookshopweb.controller;

import com.example.bookshopweb.entity.Book;
import com.example.bookshopweb.entity.User;
import com.example.bookshopweb.service.BookService;
import com.example.bookshopweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/* BookController: A könyvek kezeléséhez szükséges vezérlőosztály
*  Hozzá tartozó osztályok: BookService, UserRepository
* */
@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final UserRepository userRepository; // UserRepository injektálása

    // Konstruktor, amely injektálja a BookService-t és a UserRepository-t
    @Autowired
    public BookController(BookService bookService, UserRepository userRepository) {
        this.bookService = bookService;
        this.userRepository = userRepository;
    }

    // Könyvek listázása
    @GetMapping("/list")
    public String listBooks(Model model) {
        // A bejelentkezett felhasználó lekérdezése
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // A felhasználó lekérése a UserRepository-ból
        User currentUser = userRepository.findByUsername(username);
        if (currentUser != null) {
            model.addAttribute("books", bookService.getBooksByUser(currentUser));
        } else {
            model.addAttribute("books", bookService.getAllBooks());
        }
        return "book-list";
    }

    // Új könyv hozzáadása (GET: megjeleníti az űrlapot)
    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    // Új könyv hozzáadása (POST) - Felhasználó hozzárendelése
    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book) {
        // A bejelentkezett felhasználó lekérdezése
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        // lekéri a felhasználó teljes adatát az adatbázisból.
        User currentUser = userRepository.findByUsername(username);
        if (currentUser != null) {
            bookService.saveBook(book, currentUser);
        } else {
            bookService.saveBook(book);
        }
        return "redirect:/books/list";
    }

    // Könyv részletei
    @GetMapping("/{id}")
    public String viewBookDetails(@PathVariable Long id, Model model) {
        // Könyv lekérése az ID alapján
        Optional<Book> bookOptional = bookService.getBookById(id);
        if (bookOptional.isPresent()) {
            model.addAttribute("book", bookOptional.get());
        } else {
            model.addAttribute("errorMessage", "A keresett könyv nem található.");
        }
        return "book-detail";
    }

    // Könyv szerkesztése (GET) - Ellenőrzés, hogy a felhasználóé-e
    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        // A bejelentkezett felhasználó lekérdezése
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        // lekéri a felhasználó teljes adatát az adatbázisból.
        User currentUser = userRepository.findByUsername(username);
        // Könyv lekérése az ID alapján
        Optional<Book> bookOptional = bookService.getBookById(id);
        // Ellenőrzi, hogy a könyv létezik-e és a felhasználóé-e
        if (bookOptional.isPresent() && currentUser != null && bookOptional.get().getUser() != null && bookOptional.get().getUser().equals(currentUser)) {
            model.addAttribute("book", bookOptional.get());
            return "edit-book";
        } else {
            return "redirect:/books/list";
        }
    }

    // Könyv szerkesztése (POST) - Ellenőrzés, hogy a felhasználóé-e
    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book updatedBook) {
        // A bejelentkezett felhasználó lekérdezése
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        // lekéri a felhasználó teljes adatát az adatbázisból.
        User currentUser = userRepository.findByUsername(username);
        // Ellenőrzi, hogy a könyv létezik-e és a felhasználóé-e
        Optional<Book> existingBookOptional = bookService.getBookById(id);
        // Ha a könyv létezik, és a felhasználóé, akkor frissíti a mezőket
        if (existingBookOptional.isPresent() && currentUser != null && existingBookOptional.get().getUser() != null && existingBookOptional.get().getUser().equals(currentUser)) {
            Book existingBook = existingBookOptional.get();
            // Frissíti a mezőket
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setIsbn(updatedBook.getIsbn());
            existingBook.setDescription(updatedBook.getDescription());
            existingBook.setCoverUrl(updatedBook.getCoverUrl());
            existingBook.setPrice(updatedBook.getPrice());
            bookService.saveBook(existingBook);
            return "redirect:/books/list";
        } else {
            return "redirect:/books/list";
        }
    }

    // Könyv törlése (POST)
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        // A bejelentkezett felhasználó lekérdezése
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username);
        // Könyv lekérése az ID alapján
        Optional<Book> bookOptional = bookService.getBookById(id);
        // Ellenőrzi, hogy a könyv létezik-e és a felhasználóé-e
        if (bookOptional.isPresent() && currentUser != null && bookOptional.get().getUser() != null && bookOptional.get().getUser().equals(currentUser)) {
            bookService.deleteBook(id);
        }
        return "redirect:/books/list";
    }
}