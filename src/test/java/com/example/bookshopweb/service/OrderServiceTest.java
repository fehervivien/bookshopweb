package com.example.bookshopweb.service;

import com.example.bookshopweb.entity.Order;
import com.example.bookshopweb.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


class OrderServiceTest {

    private OrderRepository orderRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderService = new TestOrderServiceImpl(orderRepository);
    }


    @Test
    void testFindAllOrders() {
        // Készítsünk néhány rendelést
        Order order1 = new Order();
        order1.setId(1L);
        Order order2 = new Order();
        order2.setId(2L);

        // A mockolt repository visszatérítési értékeként adjuk meg a rendeléseket
        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        // Hívjuk meg a szolgáltatás findAll metódusát
        List<Order> orders = orderService.findAll();

        // Ellenőrizzük, hogy a visszaadott lista két elemet tartalmaz
        assertEquals(2, orders.size());
    }

    @Test
    void testFindOrderById() {
        // Készítsünk egy rendelést, aminek az ID-ja 1L
        Order order = new Order();
        order.setId(1L);

        // A mockolt repository-t arra kérjük, hogy amikor az 1L ID-jú rendelést keresik, azt adja vissza
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Hívjuk meg a szolgáltatás findById metódusát
        Optional<Order> result = Optional.ofNullable(orderService.findById(1L));

        // Ellenőrizzük, hogy az eredményben létezik rendelés, és annak ID-ja valóban 1L
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testSaveOrder() {
        // Készítsünk egy rendelést
        Order order = new Order();
        order.setId(1L);

        // A mockolt repository azt mondja, hogy a rendelés sikeresen elmentve
        when(orderRepository.save(order)).thenReturn(order);

        // Hívjuk meg a szolgáltatás save metódusát
        Order savedOrder = orderService.save(order);

        // Ellenőrizzük, hogy a mentett rendelés nem null és megegyezik az eredeti rendelés adataival
        assertNotNull(savedOrder);
        assertEquals(1L, savedOrder.getId());
    }

    @Test
    void testDeleteOrderById() {
        // Az OrderService deleteById metódusának tesztelése nem igényel visszatérési értéket,
        // de ellenőrizhetjük, hogy a repository deleteById metódusát meghívják.

        // Rendelés ID
        Long orderId = 1L;

        // Futtassuk le a deleteById metódust
        orderService.deleteById(orderId);

        // Ellenőrizzük, hogy a deleteById metódust meghívták a repository-ban
        verify(orderRepository, times(1)).deleteById(orderId);
    }
}
