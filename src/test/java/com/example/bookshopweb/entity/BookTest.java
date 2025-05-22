package com.example.bookshopweb.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BookTest {

    @Test
    void testGettersAndSetters() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setPrice(49.99);

        assertEquals(1L, book.getId());
        assertEquals("Effective Java", book.getTitle());
        assertEquals("Joshua Bloch", book.getAuthor());
        assertEquals(49.99, book.getPrice());
    }

    @Test
    void testConstructor() {
        Book book = new Book("Clean Code", "Robert C. Martin", 39.99);

        assertNull(book.getId()); // ID még null, mert nem állítottuk be
        assertEquals("Clean Code", book.getTitle());
        assertEquals("Robert C. Martin", book.getAuthor());
        assertEquals(39.99, book.getPrice());
    }

    @Test
    void testEqualsAndHashCode() {
        Book book1 = new Book();
        book1.setId(1L);

        Book book2 = new Book();
        book2.setId(1L);

        Book book3 = new Book();
        book3.setId(2L);

        assertEquals(book1, book2);
        assertEquals(book1.hashCode(), book2.hashCode());
        assertNotEquals(book1, book3);
    }

    @Test
    void testNotEqualsDifferentTypeOrNull() {
        Book book = new Book();
        book.setId(1L);

        assertNotEquals(book, null);      // null-lal nem egyenlő
        assertNotEquals(book, "string");  // más típussal sem
    }
}
