package com.example.bookshopweb.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/*
    A Customer osztály a vásárlókat reprezentálja az adatbázisban
    és a JPA (Java Persistence API) segítségével tárolja az adatokat.
*/

// Ez az annotáció jelzi, hogy ez az osztály egy JPA entitás,
// egy adatbázistáblát képvisel
@Entity
/* Serializable: a Customer osztály példányai bájtokká alakíthatók,
   így elmenthetők vagy továbbíthatók. (pl.: adatbázisba, fájlba mentés)
*/
public class Customer implements Serializable {

    // Az 'id' mező lesz az elsődleges kulcs (primary key)
    @Id
    // Az adatbázis automatikusan generálja az ID-t (pl. AUTO_INCREMENT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // Az 'id' mező egyedi azonosítója a vásárlónak
    private Long id;

    private String name;   // A vásárló neve
    private String email;  // A vásárló e-mail címe

    // Alapértelmezett konstruktor
    //(JPA-hoz szükséges, hogy üresen példányosítani lehessen)
    public Customer() {
    }

    // Paraméteres konstruktor az új vásárlók egyszerű létrehozásához
    // (ID nélkül, mert azt az adatbázis generálja)
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

    // equals() és hashCode() metódusokat felüldefiniáljuk,
    // hogy két Customer objektum összehasonlítható legyen ID alapján
    @Override
    public boolean equals(Object o) {
        // ha ugyanaz az objektum akkor true
        if (this == o)
            return true;
        // ha null vagy nem ugyanaz a típus akkor false
        if (o == null || getClass() != o.getClass())
            return false;
        /* Cast-eljük (átalakítás) az objektumot Customer típusra,
           hogy utána a customer.id elérhető legyen.*/
        Customer customer = (Customer) o;

        // Csak az ID alapján hasonlítjuk össze
        return Objects.equals(id, customer.id);
    }

    // hashCode() metódus, amely biztosítja a vásárló egyediségét az ID alapján
    @Override
    public int hashCode() {
        // hashCode ID alapján generálódik
        return Objects.hash(id);
    }
}
