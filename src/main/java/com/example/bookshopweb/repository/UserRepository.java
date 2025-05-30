package com.example.bookshopweb.repository;

import com.example.bookshopweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository interfész: A felhasználók adatainak kezelésére szolgál.
 * Kiterjeszti a JpaRepository-t, amely lehetővé teszi az alapvető CRUD műveletek
 * végrehajtását a User entitáson.
 * Hozzá tartozó osztályok: User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Felhasználó keresése felhasználónév alapján
    User findByUsername(String username);
    // Ellenőrzi, hogy létezik-e már a felhasználónév
    boolean existsByUsername(String username);
}