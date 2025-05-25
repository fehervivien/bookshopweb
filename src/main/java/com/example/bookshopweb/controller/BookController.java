package com.example.bookshopweb.controller;

import com.example.bookshopweb.entity.Book;
import com.example.bookshopweb.entity.User; // Importáld a User entitást
import com.example.bookshopweb.service.BookService;
import com.example.bookshopweb.repository.UserRepository; // Importáld a UserRepository-t
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // Importáld az Authentication-t
import org.springframework.security.core.context.SecurityContextHolder; // Importáld a SecurityContextHolder-t
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final UserRepository userRepository; // UserRepository injektálása

    @Autowired
    public BookController(BookService bookService, UserRepository userRepository) {
        this.bookService = bookService;
        this.userRepository = userRepository;
    }

    // Könyvek listázása - Csak a bejelentkezett felhasználó könyvei
    @GetMapping("/list")
    public String listBooks(Model model) {
        // A bejelentkezett felhasználó lekérdezése
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Ez a bejelentkezett felhasználóneve

        User currentUser = userRepository.findByUsername(username); // Felhasználó objektum lekérése
        if (currentUser != null) {
            model.addAttribute("books", bookService.getBooksByUser(currentUser)); // Szűrt lista
        } else {
            // Ez elvileg nem fordulhat elő, ha a felhasználó hitelesített
            model.addAttribute("books", bookService.getAllBooks()); // Vissza az összes könyvet, ha valami hiba van a felhasználóval
        }
        return "book-list";
    }

    // Új könyv hozzáadása (GET)
    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    // Új könyv hozzáadása (POST) - Felhasználó hozzárendelése
    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User currentUser = userRepository.findByUsername(username);
        if (currentUser != null) {
            bookService.saveBook(book, currentUser); // Könyv mentése a felhasználóval
        } else {
            // Ha valamiért nincs felhasználó (nem kéne megtörténjen)
            bookService.saveBook(book); // Mentés felhasználó nélkül (ez nem jó hosszú távon)
        }
        return "redirect:/books/list";
    }

    // Könyv részletei (nincs változás, de itt is lehetne ellenőrizni, hogy a felhasználóhoz tartozik-e)
    @GetMapping("/{id}")
    public String viewBookDetails(@PathVariable Long id, Model model) {
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username);

        Optional<Book> bookOptional = bookService.getBookById(id);
        if (bookOptional.isPresent() && currentUser != null && bookOptional.get().getUser() != null && bookOptional.get().getUser().equals(currentUser)) {
            model.addAttribute("book", bookOptional.get());
            return "edit-book";
        } else {
            // Ha a könyv nem található, vagy nem a felhasználóé
            return "redirect:/books/list"; // Vissza a listához
        }
    }

    // Könyv szerkesztése (POST) - Ellenőrzés, hogy a felhasználóé-e
    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book updatedBook) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username);

        Optional<Book> existingBookOptional = bookService.getBookById(id);
        if (existingBookOptional.isPresent() && currentUser != null && existingBookOptional.get().getUser() != null && existingBookOptional.get().getUser().equals(currentUser)) {
            Book existingBook = existingBookOptional.get();
            // Frissítsd a mezőket
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setIsbn(updatedBook.getIsbn());
            existingBook.setDescription(updatedBook.getDescription());
            existingBook.setCoverUrl(updatedBook.getCoverUrl());
            existingBook.setPrice(updatedBook.getPrice());
            // A felhasználó nem változhat meg
            bookService.saveBook(existingBook);
            return "redirect:/books/list";
        } else {
            return "redirect:/books/list"; // Nincs jogosultság vagy nem található
        }
    }

    // Könyv törlése (POST) - Ellenőrzés, hogy a felhasználóé-e
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username);

        Optional<Book> bookOptional = bookService.getBookById(id);
        if (bookOptional.isPresent() && currentUser != null && bookOptional.get().getUser() != null && bookOptional.get().getUser().equals(currentUser)) {
            bookService.deleteBook(id);
        }
        return "redirect:/books/list";
    }
}