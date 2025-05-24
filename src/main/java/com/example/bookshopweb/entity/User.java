package com.example.bookshopweb.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // A felhasználónév legyen egyedi
    private String username;

    private String password; // A jelszót hashelve tároljuk!

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Opcionális: Szerepkörök, ha később bevezeted
    // private String role; // pl. "ROLE_USER", "ROLE_ADMIN"

    // Konstruktorok (opcionális, de hasznos)
    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}