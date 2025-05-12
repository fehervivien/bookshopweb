/*package com.example.bookshopweb.controller;

import com.example.bookshopweb.entity.Order;
import com.example.bookshopweb.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // MockMvc beállítása a standalone controller-teszteléshez
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void testGetOrderById() throws Exception {
        // Given
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        when(orderService.findById(orderId)).thenReturn(order);

        // When & Then
        mockMvc.perform(get("/orders/{id}", orderId))
                .andExpect(status().isOk());

        // Ellenőrizzük, hogy a service megfelelően hívódott meg
        verify(orderService).findById(orderId);
    }
}
*/