package com.example.bookshopweb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookshopweb.entity.Book;
import com.example.bookshopweb.repository.BookRepository;

/*
 * A BookService osztály a könyvek kezeléséért felelős
 * Ez az osztály tartalmazza a könyvek mentésére,
 * lekérdezésére, frissítésére és törlésére vonatkozó
 * logikát.
*/

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Új könyv mentése az adatbázisba
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    // Az összes könyv lekérése
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Egy könyv lekérése ID alapján
    public Book getBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElse(null); //
    }

    // Könyv frissítése meglévő ID alapján
    public Book updateBook(Long id, Book book) {
        if (bookRepository.existsById(id)) {
            book.setId(id);
            return bookRepository.save(book);
        }
        return null;
    }

    // Könyv törlése ID alapján
    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
