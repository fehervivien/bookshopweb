/*package com.example.bookshopweb.repository;

import com.example.bookshopweb.entity.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("Teszt: Vásárló mentése és lekérdezése ID alapján")
    void testSaveAndFindById() {
        Customer customer = new Customer("Teszt Elek", "teszt@teszt.hu");
        customerRepository.save(customer);

        Optional<Customer> foundCustomer = customerRepository.findById(customer.getId());

        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get().getName()).isEqualTo("Teszt Elek");
        assertThat(foundCustomer.get().getEmail()).isEqualTo("teszt@teszt.hu");
    }

    @Test
    @DisplayName("Teszt: Vásárló keresése e-mail alapján")
    void testFindByEmail() {
        Customer customer = new Customer("Gipsz Jakab", "gipsz@teszt.hu");
        customerRepository.save(customer);

        Optional<Customer> foundCustomer = customerRepository.findByEmail("gipsz@teszt.hu");

        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get().getName()).isEqualTo("Gipsz Jakab");
    }

    @Test
    @DisplayName("Teszt: Vásárló törlése")
    void testDeleteCustomer() {
        Customer customer = new Customer("Törlés Teszt", "torles@teszt.hu");
        customerRepository.save(customer);

        customerRepository.delete(customer);

        Optional<Customer> deletedCustomer = customerRepository.findById(customer.getId());

        assertThat(deletedCustomer).isNotPresent();
    }
}*/
