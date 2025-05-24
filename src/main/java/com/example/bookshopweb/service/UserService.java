package com.example.bookshopweb.service;

import com.example.bookshopweb.entity.User;

public interface UserService {
    User registerNewUser(User user);
    User findByUsername(String username);
}