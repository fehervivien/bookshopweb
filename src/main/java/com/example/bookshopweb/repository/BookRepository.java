package com.example.bookshopweb.repository;

import com.example.bookshopweb.entity.Book;
import com.example.bookshopweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * BookRepository interfész: A könyvek adatainak kezelésére szolgál.
 * Kiterjeszti a JpaRepository-t, amely lehetővé teszi az alapvető CRUD műveletek
 * végrehajtását a Book entitáson.
 * Hozzá tartozó osztályok: Book, User
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // Könyvek lekérése felhasználó alapján
    List<Book> findByUser(User user);
}