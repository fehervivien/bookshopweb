package com.example.bookshopweb.security;

import com.example.bookshopweb.entity.User;
import com.example.bookshopweb.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/* A SecurityConfig osztály tesztelésére szolgáló tesztosztály
 * Hozzá tartozó osztályok: UserRepository, User
*/

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    private UserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    // Minden teszt előtt beállítja a mock UserDetailsService-t
    void setupMockUserDetailsService() {
        userDetailsService = mock(UserDetailsService.class);
        String encodedTestPass = passwordEncoder.encode("testpass");
        String encodedAdminPass = passwordEncoder.encode("adminpass");

        org.springframework.security.core.userdetails.User testUser =
                new org.springframework.security.core.userdetails.User(
                        "testuser",
                        encodedTestPass,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );

        org.springframework.security.core.userdetails.User testAdmin =
                new org.springframework.security.core.userdetails.User(
                        "testadmin",
                        encodedAdminPass,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );

        when(userDetailsService.loadUserByUsername("testuser"))
                .thenReturn(testUser);
        when(userDetailsService.loadUserByUsername("testadmin"))
                .thenReturn(testAdmin);
        when(userDetailsService.loadUserByUsername("wronguser"))
                .thenReturn(null);

        User userEntityTest = new User();
        userEntityTest.setUsername("testuser");
        userEntityTest.setPassword(encodedTestPass);
        userEntityTest.setRoles("ROLE_USER");
        userEntityTest.setId(1L);

        User userEntityAdmin = new User();
        userEntityAdmin.setUsername("testadmin");
        userEntityAdmin.setPassword(encodedAdminPass);
        userEntityAdmin.setRoles("ROLE_ADMIN");
        userEntityAdmin.setId(2L);

        when(userRepository.findByUsername("testuser"))
                .thenReturn(userEntityTest);
        when(userRepository.findByUsername("testadmin"))
                .thenReturn(userEntityAdmin);
        when(userRepository.findByUsername("wronguser"))
                .thenReturn(null);
    }


    @Test
    @DisplayName("PasswordEncoder bean should be BCryptPasswordEncoder")
    // Ellenőrzi, hogy a PasswordEncoder bean típusa BCryptPasswordEncoder,
    // amely a jelszavak titkosítására szolgál.
    void passwordEncoderBeanTest(@Autowired PasswordEncoder passwordEncoder) {
        assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

    @Test
    @DisplayName("Public endpoints should permit all access")
    // Ellenőrzi, hogy a nyilvános végpontok (pl. /login, /register)
    // mindenki számára elérhetők.
    void publicEndpointsPermitAll() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/style.css"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/header.jpg"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/book.jpg"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/hatter.jpg"))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Secured endpoints should redirect to login for unauthenticated users")
    // Ellenőrzi, hogy a védett végpontok (pl. /books/list)
    // átirányítanak a bejelentkezési oldalra, ha a felhasználó
    // nincs bejelentkezve.
    void securedEndpointsRequireAuthentication() throws Exception {
        mockMvc.perform(get("/books/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("Secured endpoints should be accessible for authenticated users")
    @WithMockUser(username = "testuser", roles = "USER")
    // Ellenőrzi, hogy a védett végpontok elérhetők-e
    // a bejelentkezett felhasználók számára.
    void securedEndpointsAccessibleWithAuthentication() throws Exception {
        mockMvc.perform(get("/books/list"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Login with valid credentials should succeed and redirect to defaultSuccessUrl")
    // Ellenőrzi, hogy a helyes felhasználónévvel és jelszóval
    // történő bejelentkezés sikeres-e
    void loginWithValidCredentials() throws Exception {
        mockMvc.perform(formLogin("/login").user("testuser").password("testpass"))
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/books/list"));
    }

    @Test
    @DisplayName("Login with invalid credentials should fail and redirect to login?error")
    // Ellenőrzi, hogy a hibás felhasználónévvel vagy jelszóval
    // történő bejelentkezés sikertelen-e, és átirányítja a felhasználót
    void loginWithInvalidCredentials() throws Exception {
        mockMvc.perform(formLogin("/login").user("testuser").password("wrongpass"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    @DisplayName("Logout should redirect to logoutSuccessUrl")
    @WithMockUser(username = "testuser", roles = "USER")
    // Ellenőrzi, hogy a kijelentkezés sikeres-e,
    // és átirányítja a felhasználót
    void logoutShouldRedirectToLoginWithLogoutParam() throws Exception {
        mockMvc.perform(logout("/logout"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?logout"));
    }

    @Test
    @DisplayName("POST to logout with valid CSRF token should succeed and redirect")
    @WithMockUser(username = "testuser", roles = "USER")
    // Ellenőrzi, hogy a kijelentkezés POST kéréssel
    // és érvényes CSRF tokennel sikeres-e,
    // és átirányítja a felhasználót a kijelentkezési oldalra.
    void logoutPostWithValidCsrfShouldRedirect() throws Exception {
        mockMvc.perform(post("/logout").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?logout"));
    }

    @Test
    @DisplayName("POST to logout without CSRF token should be forbidden")
    @WithMockUser(username = "testuser", roles = "USER")
    // Ellenőrzi, hogy a kijelentkezés POST kérése
    // érvényes CSRF token nélkül tiltott-e,
    // és 403-as státuszkódot ad vissza.
    void logoutPostWithoutCsrfShouldBeForbidden() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().isForbidden());
    }
}