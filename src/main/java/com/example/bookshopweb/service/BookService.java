package com.example.bookshopweb.service;

import com.example.bookshopweb.entity.Book;
import com.example.bookshopweb.entity.User;
import com.example.bookshopweb.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * BookService osztály: A könyvek adatainak kezelésére szolgál.
 * Ez az osztály tartalmazza a könyvek CRUD műveleteit, valamint a felhasználóval
 * kapcsolatos műveleteket is.
 * Hozzá tartozó osztályok: Book, User, BookRepository
 */
@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // Könyv mentése a felhasználóval
    public void saveBook(Book book, User user) {
        book.setUser(user);
        bookRepository.save(book);
    }

    // Könyvek lekérése felhasználó alapján
    public List<Book> getBooksByUser(User user) {
        return bookRepository.findByUser(user);
    }
}