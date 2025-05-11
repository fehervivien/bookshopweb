package com.example.bookshopweb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookshopweb.entity.Book;
import com.example.bookshopweb.repository.BookRepository;

/*
    A BookService osztály a könyvek kezeléséért felelős
    Ez az osztály tartalmazza a könyvek mentésére, lekérdezésére,
    frissítésére és törlésére vonatkozó logikát.
*/

// Ez az annotáció azt jelzi a Spring Boot számára,
// hogy ez egy "szolgáltatás" osztály, amit a Spring kezelni fog.
@Service
public class BookService {

    private final BookRepository bookRepository;

    // A Spring automatikusan beadja a BookRepository példányt
    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Új könyv mentése az adatbázisba
    public Book saveBook(Book book) {
        // A .save() metódus automatikusan menti, vagy frissíti az entitást.
        return bookRepository.save(book);
    }

    // Az összes könyv lekérése
    public List<Book> getAllBooks() {
        // Visszaadja az összes könyvet a táblából.
        return bookRepository.findAll();
    }

    // Egy könyv lekérése ID alapján
    public Book getBookById(Long id) {
        // Az Optional lehet üres is, ha nincs ilyen ID-jű könyv
        Optional<Book> book = bookRepository.findById(id);
        // Ha van találat, visszaadja, ha nincs, akkor null-t
        return book.orElse(null); //
    }

    // Könyv frissítése meglévő ID alapján
    public Book updateBook(Long id, Book book) {
        // Csak akkor frissítjük, ha a könyv létezik
        if (bookRepository.existsById(id)) {
            // Biztosítjuk, hogy az ID egyezzen
            book.setId(id);
            // Mentjük a módosított könyvet
            return bookRepository.save(book);
        }
        return null; // Ha nincs ilyen ID-jű könyv, akkor null
    }

    // Könyv törlése ID alapján
    public boolean deleteBook(Long id) {
        // Ellenőrizzük, hogy létezik-e a könyv
        if (bookRepository.existsById(id)) {
            // Ha igen, töröljük
            bookRepository.deleteById(id);
            return true;
        }
        return false; // Ha nem létezik, akkor nem törlünk semmit
    }
}
