package com.example.bookshopweb.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CustomerTest {

    @Test
    void testGettersAndSetters() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");

        assertEquals(1L, customer.getId());
        assertEquals("John Doe", customer.getName());
        assertEquals("john.doe@example.com", customer.getEmail());
    }

    @Test
    void testConstructor() {
        Customer customer = new Customer("Jane Smith", "jane.smith@example.com");

        assertNull(customer.getId()); // ID nincs beállítva a konstruktorban
        assertEquals("Jane Smith", customer.getName());
        assertEquals("jane.smith@example.com", customer.getEmail());
    }

    @Test
    void testEqualsAndHashCode() {
        Customer customer1 = new Customer();
        customer1.setId(1L);

        Customer customer2 = new Customer();
        customer2.setId(1L);

        Customer customer3 = new Customer();
        customer3.setId(2L);

        assertEquals(customer1, customer2);
        assertEquals(customer1.hashCode(), customer2.hashCode());
        assertNotEquals(customer1, customer3);
    }

    @Test
    void testNotEqualsDifferentTypeOrNull() {
        Customer customer = new Customer();
        customer.setId(1L);

        assertNotEquals(customer, null);
        assertNotEquals(customer, "string");
    }
}

