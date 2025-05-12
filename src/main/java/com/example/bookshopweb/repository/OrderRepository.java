package com.example.bookshopweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshopweb.entity.Order;

/*
 * Az OrderRepository egy interfész, amely az adatbázisban tárolt
 * rendelések kezelésére szolgál.
 * Örökli a JpaRepository összes funkcióját.
*/
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}

