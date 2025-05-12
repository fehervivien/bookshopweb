package com.example.bookshopweb.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/*
 * A Customer osztály a vásárlókat reprezentálja az adatbázisban
 * és a JPA (Java Persistence API) segítségével tárolja az adatokat.
 * Serializable interfész: Az objektumok sorosíthatók legyenek
 * (mentés fájlba vagy adatbázisba).
*/


@Entity
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;   // A vásárló neve
    private String email;  // A vásárló e-mail címe

    // Alapértelmezett konstruktor (JPA-hoz szükséges)
    public Customer() {
    }

    // Paraméteres konstruktor az új vásárlók egyszerű
    // létrehozásához
    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getterek és setterek
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    // a vásárlók összehasonlításához szükséges metódus
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    // hashCode() metódus, amely biztosítja a vásárló
    // egyediségét az ID alapján
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
