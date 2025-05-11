package com.example.bookshopweb.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
/*
 * A Book osztály a könyvek entitását reprezentálja az adatbázisban.
 * Az osztály tartalmazza a könyv adatait, mint például a címét, szerzőjét
 * és árát.
 * A JPA annotációk segítségével az osztály összekapcsolható az adatbázissal.
 */

/* Az osztály annotációval van ellátva, amely jelzi, hogy ez egy JPA entitás,
 az osztály egy adatbázis táblát reprezentál.
 A JPA (Java Persistence API) egy Java specifikáció, amely lehetővé teszi
 az objektumok és relációs adatbázisok közötti leképezést.
*/
@Entity

/* Az osztály implementálja a Serializable interfészt, hogy az objektumok
sorosíthatók legyenek (mentés fájlba vagy adatbázisba, bájtfolyammá alakítja). */
public class Book implements Serializable { //

    // Az 'id' mező lesz az elsődleges kulcs (primary key)
    @Id

    // Az adatbázis automatikusan generálja az ID-t (pl. AUTO_INCREMENT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id; // Az 'id' mező egyedi azonosítója a könyvnek
    private String title; // A könyv címe
    private String author; // A könyv szerzője
    private double price; // A könyv ára

    // Alapértelmezett konstruktor (JPA-hoz szükséges, hogy üresen
    // is létre lehessen hozni az objektumot)
    public Book() {
    }

    // Paraméteres konstruktor az új könyvek egyszerű létrehozásához
    public Book(String title, String author, double price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

    // Getterek és setterek
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Az equals() és hashCode() metódusokat felüldefiniáljuk,
    // hogy a könyvek összehasonlítása és hash kód generálása helyesen működjön.
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        // a paramétert Book típusúra konvertáljuk
        // hogy elérhessük a Book osztály mezőit
        Book book = (Book) o;
        // összehasonlítjuk az id mezőket
        return Objects.equals(id, book.id);
    }

    // A hashCode() metódus a könyv azonosítóját használja
    // a hash kód generálásához.
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
