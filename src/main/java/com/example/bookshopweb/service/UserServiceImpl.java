// src/main/java/com/example/bookshopweb/service/UserServiceImpl.java
package com.example.bookshopweb.service;

import com.example.bookshopweb.entity.User;
import com.example.bookshopweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Szükséges a jelszó hasheléséhez
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Adatbázis műveletekhez

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Ezt Spring Security biztosítja majd

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional // Tranzakciókezelés a mentéshez
    public User registerNewUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("A felhasználónév már foglalt: " + user.getUsername());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Jelszó hashelése
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}