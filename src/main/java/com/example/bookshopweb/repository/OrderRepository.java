package com.example.bookshopweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookshopweb.entity.Order;

/*
   Az OrderRepository egy interfész, amely az adatbázisban tárolt rendelések
   (Order entitások) kezelésére szolgál. Ez az interfész nem tartalmaz
   saját metódusokat, hanem örökli a JpaRepository összes funkcióját.
   A Spring magától írja meg.
*/
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}

