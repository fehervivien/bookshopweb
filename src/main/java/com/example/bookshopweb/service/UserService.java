package com.example.bookshopweb.service;

import com.example.bookshopweb.entity.User;


/**
 * UserService interfész: A felhasználók adatainak kezelésére szolgál.
 * Ez az interfész tartalmazza a felhasználók regisztrációját és keresését.
 * Hozzá tartozó osztályok: User
 */
public interface UserService {
    User registerNewUser(User user);
    User findByUsername(String username);
}