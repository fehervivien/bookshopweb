package com.example.bookshopweb.entity;

import jakarta.persistence.*;

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

    // Új mező: Kapcsolat a felhasználóval
    @ManyToOne(fetch = FetchType.LAZY) // Sok könyv tartozik egy felhasználóhoz
    @JoinColumn(name = "user_id") // A foreign key oszlop neve az 'books' táblában
    private User user; // A könyvet hozzáadó felhasználó

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
}