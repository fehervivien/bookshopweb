package com.example.bookshopweb.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * BookTest: A Book entitás tesztelésére szolgáló osztály.
 * Ez az osztály ellenőrzi a Book osztály konstruktorait, gettereit és settereit.
 * Hozzá tartozó osztályok: Book, User
 */

public class BookTest {

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(100L);
        testUser.setUsername("testuser");
        testUser.setPassword("encoded_test_password");
    }

    @Test
    // Ellenőrzi az alapértelmezett konstruktor működését, a mezők null értékét.
    void testDefaultConstructor() {
        Book book = new Book();
        assertNull(book.getId(), "Az ID-nek null-nak kell lennie az alapértelmezett konstruktor után.");
        assertNull(book.getTitle(), "A Title-nek null-nak kell lennie.");
        assertNull(book.getAuthor(), "Az Author-nak null-nak kell lennie.");
        assertNull(book.getIsbn(), "Az ISBN-nek null-nak kell lennie.");
        assertNull(book.getDescription(), "A Description-nek null-nak kell lennie.");
        assertNull(book.getCoverUrl(), "A CoverUrl-nek null-nak kell lennie.");
        assertNull(book.getPrice(), "A Price-nak null-nak kell lennie.");
        assertNull(book.getUser(), "A User-nek null-nak kell lennie.");
    }

    @Test
    // Teszteli a paraméterezett konstruktort, amely beállítja a könyv mezőit.
    void testParameterizedConstructor() {
        String title = "A Gyűrűk Ura";
        String author = "J.R.R. Tolkien";
        String isbn = "978-0618053267";
        String description = "Egy fantasy eposz.";
        String coverUrl = "http://example.com/lotr_cover.jpg";
        Double price = 25.50;
        Book book = new Book(title, author, isbn, description, coverUrl, price);

        assertNull(book.getId(), "Az ID-nek null-nak kell lennie, mivel a paraméterezett konstruktor nem állítja be.");
        assertEquals(title, book.getTitle(), "A Title nem egyezik.");
        assertEquals(author, book.getAuthor(), "Az Author nem egyezik.");
        assertEquals(isbn, book.getIsbn(), "Az ISBN nem egyezik.");
        assertEquals(description, book.getDescription(), "A Description nem egyezik.");
        assertEquals(coverUrl, book.getCoverUrl(), "A CoverUrl nem egyezik.");
        assertEquals(price, book.getPrice(), "A Price nem egyezik.");
        assertNull(book.getUser(), "A User-nek null-nak kell lennie, mivel a paraméterezett konstruktor nem állítja be.");
    }

    @Test
    // Teszteli a paraméterezett konstruktort, amely beállítja
    // a könyv mezőit és a felhasználót.
    void testGettersAndSetters() {
        Book book = new Book();
        Long id = 1L;
        String title = "A hobbit";
        String author = "J.R.R. Tolkien";
        String isbn = "978-0345339683";
        String description = "Egy hobbit kalandjai.";
        String coverUrl = "http://example.com/hobbit_cover.jpg";
        Double price = 15.75;

        book.setId(id);
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setDescription(description);
        book.setCoverUrl(coverUrl);
        book.setPrice(price);
        book.setUser(testUser);

        // Ellenőrizzük a gettereket és settereket
        assertEquals(id, book.getId(), "Az ID getter/setter hibás.");
        assertEquals(title, book.getTitle(), "A Title getter/setter hibás.");
        assertEquals(author, book.getAuthor(), "Az Author getter/setter hibás.");
        assertEquals(isbn, book.getIsbn(), "Az ISBN getter/setter hibás.");
        assertEquals(description, book.getDescription(), "A Description getter/setter hibás.");
        assertEquals(coverUrl, book.getCoverUrl(), "A CoverUrl getter/setter hibás.");
        assertEquals(price, book.getPrice(), "A Price getter/setter hibás.");

        // Ellenőrzi a felhasználó mezőt
        assertNotNull(book.getUser(), "A User nem lehet null.");
        assertEquals(testUser, book.getUser(), "A User objektum nem egyezik.");
        assertEquals(testUser.getId(), book.getUser().getId(), "A User ID nem egyezik.");
        assertEquals(testUser.getUsername(), book.getUser().getUsername(), "A User username nem egyezik.");
    }
}