package com.example.bookshopweb.entity;

import jakarta.persistence.*;
import java.util.Objects; // Importáld az Objects osztályt

/**
 * A Book osztály: A könyvek adatainak tárolására szolgál.
 * Minden könyv rendelkezik egyedi azonosítóval, címmel, szerzővel,
 * ISBN számmal, leírással, borító URL-lel és árral.
 * Ezen kívül kapcsolódik egy felhasználóhoz, aki hozzáadta a könyvet.
 * Hozzá tartozó osztályok: User
 */
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private String coverUrl;
    private Double price;

    // Sok könyv tartozik egy felhasználóhoz
    // LAZY: a felhasználó adatai csak akkor töltődnek be,
    // amikor ténylegesen szükség van rájuk.
    @ManyToOne(fetch = FetchType.LAZY)
    // A könyvet hozzáadó felhasználó azonosítója
    @JoinColumn(name = "user_id")
    // A könyvet hozzáadó felhasználó
    private User user;

    // --- Konstruktorok ---
    public Book() {
    }

    public Book(String title, String author, String isbn, String description, String coverUrl, Double price) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.description = description;
        this.coverUrl = coverUrl;
        this.price = price;
    }

    // --- Getterek és Setterek ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    // Új getter és setter a felhasználóhoz
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // equals metódus: az ID alapján hasonlítja össze a könyveket.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id != null && Objects.equals(id, book.id);
    }

    // hashCode metódus: Visszaadja a felhasználó azonosítójának (id)
    // hash kódját.
    @Override
    public int hashCode() {
       return Objects.hash(id);
    }
}