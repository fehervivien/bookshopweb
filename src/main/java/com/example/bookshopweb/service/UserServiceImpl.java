package com.example.bookshopweb.service;

import com.example.bookshopweb.entity.User;
import com.example.bookshopweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails; // ÚJ IMPORT
import org.springframework.security.core.userdetails.UserDetailsService; // ÚJ IMPORT
import org.springframework.security.core.userdetails.UsernameNotFoundException; // ÚJ IMPORT
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections; // Szükséges lehet az üres jogosultságlistához

@Service
// A UserDetailsService interfészt kell implementálni a Spring Security számára
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User registerNewUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) { // existsByUsername helyett findByUsername
            throw new RuntimeException("A felhasználónév már foglalt: " + user.getUsername());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Jelszó hashelése
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // EZ AZ ÚJ METÓDUS A SPRING SECURITY RÉSZÉHEZ
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Nincs ilyen felhasználó: " + username);
        }
        // Itt hozzuk létre a Spring Security által elvárt UserDetails objektumot.
        // Ebben az esetben egyszerűen beállítjuk a felhasználónevet, a már titkosított jelszót,
        // és egy üres jogosultságlistát (ROLE_USER-t adhatsz hozzá, ha szükséges).
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList() // Itt adhatsz hozzá szerepeket, pl. List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}