package com.example.bookshopweb.entity;

import jakarta.persistence.*;
import java.util.Objects; // Importáld az Objects osztályt

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String roles; // Ha szerepköröket is használsz

    // Konstruktorok
    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    // Ha szerepköröket is használsz:
    public User(String username, String password, String roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    // Getterek és setterek
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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    // FONTOS: equals() és hashCode() metódusok a User entitáshoz
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id); // Összehasonlítás az ID alapján
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Hash az ID alapján
    }
}