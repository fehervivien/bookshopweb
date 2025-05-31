package com.example.bookshopweb.service;

import com.example.bookshopweb.entity.User;
import com.example.bookshopweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;

/*
 * UserServiceImpl osztály: A felhasználók kezelésére szolgáló szolgáltatás
 * implementációja.
 * Implementálja a UserService és UserDetailsService Spring Security interfészeket.
 * Hozzá tartozó osztályok: User, UserRepository
 */

// UserDetailsService: A felhasználói adatok betöltésére szolgál.
// UserService: A felhasználók kezelésére szolgál.
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Konstruktor, amely inicializálja a UserRepository-t és a PasswordEncoder-t
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    // Regisztrál egy új felhasználót
    public User registerNewUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("A felhasználónév már foglalt: " + user.getUsername());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    // A felhasználó keresése a felhasználónév alapján
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    // Betölti a felhasználói adatokat
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Nincs ilyen felhasználó: " + username);
        }
        // Visszaadja a felhasználó adatait UserDetails formátumban
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}