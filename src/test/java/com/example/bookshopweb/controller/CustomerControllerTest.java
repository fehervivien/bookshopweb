package com.example.bookshopweb.controller;

import com.example.bookshopweb.entity.Customer;
import com.example.bookshopweb.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(customer));

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer-list"))
                .andExpect(model().attributeExists("customers"));
    }

    @Test
    public void testGetCustomerById_Found() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customer));

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer-detail"))
                .andExpect(model().attributeExists("customer"));
    }

    @Test
    public void testGetCustomerById_NotFound() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customers"));
    }

    @Test
    public void testCreateCustomer() throws Exception {
        mockMvc.perform(post("/customers"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customers"));

        verify(customerService, times(1)).saveCustomer(any(Customer.class));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/customers/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customers"));

        verify(customerService, times(1)).deleteCustomer(1L);
    }
}
