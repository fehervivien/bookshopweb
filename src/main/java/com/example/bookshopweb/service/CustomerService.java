package com.example.bookshopweb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookshopweb.entity.Customer;
import com.example.bookshopweb.repository.CustomerRepository;

/*
 * A CustomerService osztály a vásárlók kezeléséért felelős
 * Ez az osztály tartalmazza a vásárlók mentésére, lekérdezésére,
 * frissítésére és törlésére vonatkozó logikát.
*/

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Új customer mentése
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // Összes customer lekérdezése
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Egy customer lekérdezése ID alapján
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    // Customer törlése ID alapján
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    // Email alapján keresés (ha kellene valahol)
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}
