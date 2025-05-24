// src/main/java/com/example/bookshopweb/security/SecurityConfig.java
package com.example.bookshopweb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Egy jó jelszó hasháló
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.bookshopweb.repository.UserRepository;

@Configuration
@EnableWebSecurity // Engedélyezi a Spring Security-t
public class SecurityConfig {

    // Jelszó encoder, amit a jelszavak hashelésére és ellenőrzésére használunk
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // A UserDetailsService felelős a felhasználók adatbázisból való betöltéséért
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            com.example.bookshopweb.entity.User user = userRepository.findByUsername(username);
            if (user != null) {
                // Itt építjük fel a Spring Security User objektumát
                // Ha szerepköröket is használsz, itt adnád hozzá őket (pl. AuthorityUtils.createAuthorityList("ROLE_USER"))
                return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles("USER") // Alapértelmezett szerepkör, ha nincs a User entitásban
                        .build();
            }
            throw new UsernameNotFoundException("Felhasználó nem található: " + username);
        };
    }

    // A SecurityFilterChain konfigurálja a HTTP kérések biztonsági szabályait
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/register", "/login", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll() // Ezek az URL-ek mindenki számára elérhetők
                        .anyRequest().authenticated() // Minden más kéréshez hitelesítés szükséges
                )
                .formLogin(formLogin -> formLogin // Bejelentkezési űrlap konfigurációja
                        .loginPage("/login") // A te egyedi bejelentkezési oldalad
                        .defaultSuccessUrl("/books/list", true) // Sikeres bejelentkezés után ide irányít
                        .failureUrl("/login?error=true") // Sikertelen bejelentkezés után ide irányít
                        .permitAll() // A bejelentkezési oldal mindenki számára elérhető
                )
                .logout(logout -> logout // Kijelentkezés konfigurációja
                        .logoutSuccessUrl("/login?logout=true") // Kijelentkezés után ide irányít
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()) // Ideiglenesen kikapcsoljuk a CSRF védelmet (fejlesztéshez)
                // Éles környezetben NEM ajánlott kikapcsolni!
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())); // H2-console miatt fontos

        return http.build();
    }
}