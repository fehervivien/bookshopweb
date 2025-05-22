package com.example.bookshopweb.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    void testGettersAndSetters() {
        Customer customer = new Customer("John Doe", "john.doe@example.com");
        Book book = new Book("Example Book", "Author Name", 19.99);

        Order order = new Order();
        order.setId(1L);
        order.setOrderDate(LocalDate.of(2024, 4, 26));
        order.setCustomer(customer);
        order.setBook(book);

        assertEquals(1L, order.getId());
        assertEquals(LocalDate.of(2024, 4, 26), order.getOrderDate());
        assertEquals(customer, order.getCustomer());
        assertEquals(book, order.getBook());
    }

    @Test
    void testConstructor() {
        Customer customer = new Customer("Jane Smith", "jane.smith@example.com");
        Book book = new Book("Another Book", "Another Author", 29.99);

        LocalDate orderDate = LocalDate.of(2025, 1, 15);

        Order order = new Order(orderDate, customer, book);

        assertNull(order.getId()); // ID nem kerül beállításra a konstruktorban
        assertEquals(orderDate, order.getOrderDate());
        assertEquals(customer, order.getCustomer());
        assertEquals(book, order.getBook());
    }

    @Test
    void testEqualsAndHashCode() {
        Order order1 = new Order();
        order1.setId(1L);

        Order order2 = new Order();
        order2.setId(1L);

        Order order3 = new Order();
        order3.setId(2L);

        assertEquals(order1, order2);
        assertNotEquals(order1, order3);
        assertEquals(order1.hashCode(), order2.hashCode());
    }

    @Test
    void testNotEqualsDifferentTypeOrNull() {
        Order order = new Order();
        order.setId(1L);

        assertNotEquals(order, null);
        assertNotEquals(order, "string");
    }

    @Test
    void testToString() {
        Order order = new Order();
        order.setId(5L);
        order.setOrderDate(LocalDate.of(2025, 5, 5));

        String expected = "Order{id=5, orderDate=2025-05-05}";
        assertEquals(expected, order.toString());
    }
}
