package com.example.bookshopweb.security;

import com.example.bookshopweb.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/*
* SecurityConfig osztály felelős a biztonsági konfigurációért.
* Ez a konfiguráció biztosítja, hogy a felhasználók csak a megfelelő
* jogosultságokkal rendelkező útvonalakat érhessék el.
* */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // UserDetailsService bean, amely a felhasználói adatok betöltésére szolgál.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Konfigurálja a Spring Security-t, hogy hogyan kezelje,
    // a felhasználónév/jelszó alapú bejelentkezést
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    // A webes alkalmazás biztonságának beállítása.
    // Biztonsági szűrőlánc (SecurityFilterChain): Az összes HTTP kérést feldolgozza,
    // és eldönti, hogy egy felhasználó hozzáférhet-e egy adott erőforráshoz
    // és hogyan történjen az autentikáció és a kijelentkezés.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))

                .authorizeHttpRequests(authorize -> authorize
                        // Nyilvánosan elérhető útvonalak
                        .requestMatchers(
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/register"),
                                new AntPathRequestMatcher("/style.css"),
                                new AntPathRequestMatcher("/header.jpg"),
                                new AntPathRequestMatcher("/book.jpg"),
                                new AntPathRequestMatcher("/hatter.jpg"),
                                new AntPathRequestMatcher("/h2-console/**")
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/books/list", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}