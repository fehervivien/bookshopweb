package com.example.bookshopweb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.bookshopweb.entity.Order;
import com.example.bookshopweb.repository.OrderRepository;

/*
 * Az OrderServiceImpl osztály valósítja meg azt,
 * amit az OrderService interfész előírt.
 */


@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    // Konstruktor (OrderRepository)
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Visszaadja az összes rendelést az adatbázisból
    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    // Lekérdez egy rendelést az adott ID alapján
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

    @Override
    public Optional<Order> getOrderById(long l) {
        return orderRepository.findById(l);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


}
