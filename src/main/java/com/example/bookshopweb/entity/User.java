package com.example.bookshopweb.entity;

import jakarta.persistence.*;
import java.util.Objects;

/* User osztály: a felhasználók adatainak tárolására szolgál,
 * tartalmazza a felhasználó azonosítóját, felhasználónevét,
 * jelszavát és szerepköreit.
* */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String roles;

    // Konstruktorok
    public User() {}

    // Konstruktor: a felhasználó alap bejelentkezési adatainak
    // inicializálása.(2 argumentumú konstruktor)
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    // Konstruktor: a felhasználó alap bejelentkezési adatainak
    // + szerepköreinek inicializálása. (3 argumentumú konstruktor)
    public User(String username, String password, String roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    // Getters and setters
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

    // equals: összehasonlítja a felhasználókat az azonosítójuk alapján.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    // hashCode: visszaadja a felhasználó azonosítójának hash kódját.
    @Override
    public int hashCode() {
        return id == null ? 0 : Objects.hash(id); // Handle null id
    }
}