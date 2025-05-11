package com.example.bookshopweb.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.bookshopweb.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // További egyedi lekérdezésekhez lehetőségek
}
