package com.example.bookshopweb.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*
    Az Order osztály a rendszerben rögzített könyvrendeléseket (megrendeléseket)
    képviseli (orders tábla). A JPA (Java Persistence API) segítségével tárolja
    az adatokat.
*/

// Ez az osztály egy JPA entitás (az adatbázis egyik táblájának felel meg)
@Entity
// Az adatbázisban az "orders" nevű táblához lesz leképezve
@Table(name = "orders")

public class Order {

    // Id: a tábla elsődleges kulcsa (primary key)
    @Id
    // Az adatbázis automatikusan generálja az id-t
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // Az id mező
    private Long id;
    // A rendelés dátuma
    private LocalDate orderDate;

    // Egy rendelés egy vásárlóhoz (Customer) tartozik
    @ManyToOne
    // Az adatbázisban a kapcsolódó customer_id oszlopot használja
    @JoinColumn(name = "customer_id")

    // Vásárló (Customer) entitás
    private Customer customer;

    // Egy rendelés egy könyvhöz (Book) kapcsolódik
    @ManyToOne
    // Az adatbázisban a kapcsolódó book_id oszlopot használja
    @JoinColumn(name = "book_id")
    private Book book;

    /*
     * Alapértelmezett konstruktor (JPA-hoz szükséges, hogy üresen is létre
     * lehessen hozni az objektumot)
     */
    public Order() {
    }

    // Paraméteres konstruktor
    public Order(LocalDate orderDate, Customer customer, Book book) {
        this.orderDate = orderDate;
        this.customer = customer;
        this.book = book;
    }

    // Getterek és Setterek
    public Long getId() {
        return id;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    // equals: két Order objektumot azonosnak tekintünk, ha az id-jük megegyezik
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        // a paramétert Order típusúra konvertáljuk
        Order order = (Order) o;
        return id != null && id.equals(order.id);
    }

    // hashCode: az id mező alapján generálja a hash értéket
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", orderDate=" + orderDate + "}";
    }

    public void setId(long l) {
        this.id = l;
    }
}
