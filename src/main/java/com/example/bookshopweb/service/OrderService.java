package com.example.bookshopweb.service;

import java.util.List;
import java.util.Optional;

import com.example.bookshopweb.entity.Order;

/*
    Ez az interfész meghatározza,
    hogy milyen műveletek végezhetők el a rendelések (Order) kezelésére
*/

public interface OrderService {

    // Lekérdezi az összes rendelést az adatbázisból
    List<Order> findAll();

    // Egy konkrét rendelést keres ID alapján
    Order findById(Long id);

    // Új rendelés mentése vagy meglévő rendelés frissítése
    Order save(Order order);

    // Rendelés törlése ID alapján
    void deleteById(Long id);

    Optional<Order> getOrderById(long l);

    List<Order> getAllOrders();
}
