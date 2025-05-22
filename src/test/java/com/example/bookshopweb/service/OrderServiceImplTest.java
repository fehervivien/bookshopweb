package com.example.bookshopweb.service;

import com.example.bookshopweb.entity.Book;
import com.example.bookshopweb.entity.Customer;
import com.example.bookshopweb.entity.Order;
import com.example.bookshopweb.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {

    private OrderRepository orderRepository;
    private OrderServiceImpl orderService;

    // Konkrét megvalósítás csak a teszthez
    private static class TestOrderServiceImpl extends OrderServiceImpl {
        public TestOrderServiceImpl(OrderRepository orderRepository) {
            super(orderRepository);
        }

        @Override
        public Optional<Order> getOrderById(long id) {
            return super.findAll().stream().filter(o -> o.getId() == id).findFirst();
        }

        @Override
        public List<Order> getAllOrders() {
            return super.findAll();
        }
    }

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderService = new TestOrderServiceImpl(orderRepository);
    }

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(new Order(), new Order()));
        List<Order> orders = orderService.getAllOrders();
        assertEquals(2, orders.size());
    }

    @Test
    void testGetOrderById() {
        Order order = new Order();
        // Példa ID
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.findAll()).thenReturn(List.of(order));

        Optional<Order> result = orderService.getOrderById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testSaveOrder() {
        // 1. Létrehozzuk a tesztelendő Order objektumot
        Order order = new Order();
        order.setOrderDate(LocalDate.now()); // Példa dátum
        order.setCustomer(new Customer()); // Példányosítjuk a Customer-t is
        order.setBook(new Book()); // Példányosítjuk a Book-ot is

        // 2. Meghatározzuk, hogy mi legyen a mock válasz
        when(orderRepository.save(order)).thenReturn(order);

        // 3. Meghívjuk a save() metódust a service-ben
        Order savedOrder = orderService.save(order);

        // 4. Ellenőrizzük, hogy az elmentett rendelés ugyanazt adja-e vissza
        assertNotNull(savedOrder);
        assertEquals(order, savedOrder);
        verify(orderRepository).save(order); // Ellenőrizzük, hogy a save metódus valóban meghívódott
    }

    @Test
    void testDeleteOrderById() {
        // A rendelés, amelyet törölni fogunk
        Order order = new Order();
        order.setId(1L);

        // Mockoljuk a deleteById metódust, hogy ne csináljon semmit
        doNothing().when(orderRepository).deleteById(1L);

        // Meghívjuk a deleteById metódust a service-ben
        orderService.deleteById(1L);

        // Ellenőrizzük, hogy a deleteById metódus valóban meghívódott egyszer
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindOrderByIdNotFound() {
        // Az ID, amelyhez nem tartozik rendelés
        long nonExistentId = 999L;

        // Mockoljuk, hogy a findById nem talál rendelést, így kivételt dob
        when(orderRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Meghívjuk a findById metódust a service-ben
        // Ellenőrizzük, hogy kivételt dob
        assertThrows(RuntimeException.class, () -> orderService.findById(nonExistentId),
                "Order not found with id: " + nonExistentId);
    }

    @Test
    void testFindOrderByIdThrowsExceptionWhenNotFound() {
        // A rendelés, amit keresünk
        long nonExistentOrderId = 99L;

        // A repository nem ad vissza semmit a megadott ID-hoz (képzeljük el, hogy nincs ilyen rendelés)
        when(orderRepository.findById(nonExistentOrderId)).thenReturn(Optional.empty());

        // Ellenőrizzük, hogy a RuntimeException kivétel dobódik
        assertThrows(RuntimeException.class, () -> orderService.findById(nonExistentOrderId));
    }
}
