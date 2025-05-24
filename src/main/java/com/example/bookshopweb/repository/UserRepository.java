package com.example.bookshopweb.repository;

import com.example.bookshopweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username); // Metódus a felhasználónév alapján történő kereséshez
    boolean existsByUsername(String username); // Ellenőrzés, hogy létezik-e már a felhasználónév
}