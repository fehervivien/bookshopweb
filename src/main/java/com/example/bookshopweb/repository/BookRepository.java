package com.example.bookshopweb.repository;

import com.example.bookshopweb.entity.Book;
import com.example.bookshopweb.entity.User; // Importáld a User entitást
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // Új metódus: Könyvek lekérése felhasználó alapján
    List<Book> findByUser(User user);
}