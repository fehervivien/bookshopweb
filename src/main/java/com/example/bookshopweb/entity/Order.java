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
 * Az Order osztály a rendszerben rögzített könyvrendeléseket
 * (megrendeléseket) képviseli (orders tábla).
 * A JPA (Java Persistence API) segítségével tárolja az adatokat.
*/


@Entity
@Table(name = "orders")

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private LocalDate orderDate;

    // Egy rendelés egy vásárlóhoz (Customer) tartozik
    @ManyToOne
    @JoinColumn(name = "customer_id")

    private Customer customer;

    // Egy rendelés egy könyvhöz (Book) kapcsolódik
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;


     // Alapértelmezett konstruktor (JPA-hoz szükséges)
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


    @Override
    // equals: összehasonlítja az Order objektumokat
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
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
