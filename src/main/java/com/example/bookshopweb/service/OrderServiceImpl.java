package com.example.bookshopweb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bookshopweb.entity.Order;
import com.example.bookshopweb.repository.OrderRepository;

/*
 * Az OrderServiceImpl osztály valósítja meg azt, amit az OrderService interfész
 * előírt.
 */

// Ez egy "szolgáltatás" komponens
@Service
public abstract class OrderServiceImpl implements OrderService {

    /*
     * Privát mező az OrderRepository példányhoz,
     * ezen keresztül végezzük az adatbázisműveleteket
     */
    private final OrderRepository orderRepository;

    // A Spring automatikusan beadja a OrderRepository példányt
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Visszaadja az összes rendelést az adatbázisból
    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    // Lekérdez egy rendelést az adott ID alapján
    // Ha nem található, akkor kivételt dob
    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Order not found with id: " + id));
    }

    // Elment egy új rendelést, vagy frissít egy meglévőt
    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    // Töröl egy rendelést az ID alapján
    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}
