package com.example.bookshopweb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Elsőként a H2 Console CSRF és Frame Options beállításai
                // Ez biztosítja, hogy a Spring Security még mielőtt bármilyen authentikációs logikába kezdene,
                // teljesen ignorálja a H2 konzol kéréseit a CSRF szempontjából.
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))

                // Utána jöhet a többi authorizációs szabály
                .authorizeHttpRequests(authorize -> authorize
                        // Nyilvánosan elérhető útvonalak
                        .requestMatchers(
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/register"),
                                new AntPathRequestMatcher("/css/**"),
                                new AntPathRequestMatcher("/js/**"),
                                new AntPathRequestMatcher("/images/**"),
                                new AntPathRequestMatcher("/favicon.ico"),
                                new AntPathRequestMatcher("/header.jpg"),
                                new AntPathRequestMatcher("/book.jpg"),
                                new AntPathRequestMatcher("/hatter.jpg"), // <-- EZT A SORT ADTUK HOZZÁ!
                                new AntPathRequestMatcher("/h2-console/**")
                        ).permitAll()
                        // Minden más útvonal hitelesítést igényel
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