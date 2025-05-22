package com.example.bookshopweb.service;

import com.example.bookshopweb.entity.Order;
import com.example.bookshopweb.repository.OrderRepository;
import java.util.List;
import java.util.Optional;

/*
 * Ez egy tesztosztály-specifikus leszármazott osztály,
 * amely lehetővé teszi az osztály példányosítását a tesztkörnyezetben.
 * Ez azért kell mert az OrderServiceImpl osztály egy absztrakt osztály
 * és csak igy tudjuk az alábbiakat tesztelni.
*/

class TestOrderServiceImpl extends OrderServiceImpl {

    public TestOrderServiceImpl(OrderRepository orderRepository) {
        super(orderRepository);
    }

    @Override
    public Optional<Order> getOrderById(long l) {
        // Itt visszaadhatunk egy teszt adatot, pl. egy Order példányt
        return Optional.of(new Order());
    }

    @Override
    public List<Order> getAllOrders() {
        // Itt is egy tesztadatot adhatunk vissza
        return List.of(new Order(), new Order());
    }
}
