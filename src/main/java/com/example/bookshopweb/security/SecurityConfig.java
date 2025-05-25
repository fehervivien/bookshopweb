package com.example.bookshopweb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher; // Ezt a sort ADD HOZZÁ!

import com.example.bookshopweb.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            com.example.bookshopweb.entity.User user = userRepository.findByUsername(username);
            if (user != null) {
                return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles("USER")
                        .build();
            }
            throw new UsernameNotFoundException("Felhasználó nem található: " + username);
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/",                      // Főoldal, ha nincs explicit /books/list
                                "/books/list",            // Könyvek listája (public)
                                "/books/{id}",            // Könyv részletei (public)
                                "/register",              // Regisztráció
                                "/login",                 // Bejelentkezés
                                "/css/**",                // CSS mappában lévő fájlok (ha vannak)
                                "/images/**",             // Képek mappában lévő fájlok (ha vannak)
                                "/js/**",                 // JS mappában lévő fájlok (ha vannak)
                                "/h2-console/**",         // H2 konzol
                                "/*.css",                 // **FONTOS: style.css a static gyökérben**
                                "/*.jpg",                 // **FONTOS: book.jpg, header.jpg a static gyökérben**
                                "/*.png"                  // Ha vannak png-k a static gyökérben
                        ).permitAll() // Ezek az URL-ek mindenki számára elérhetők
                        .anyRequest().authenticated() // Minden más kéréshez hitelesítés szükséges
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/books/list", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // Ezt a sort már biztosan kell, ha nincs más logout config
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}