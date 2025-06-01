package com.example.bookshopweb.controller;

import com.example.bookshopweb.entity.User;
import com.example.bookshopweb.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when; // <-- Fontos: ezt az importot is hozzá kell adni!
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
* Az AuthController osztály regisztrációs és bejelentkezési funkciók
* tesztelésére szolgál.
* */

@WebMvcTest(controllers = AuthController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
    }

    @Test
    // A regisztrációs űrlap megjelenítésére szolgáló teszt
    void showRegistrationForm_shouldReturnRegisterViewWithNewUser() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", instanceOf(User.class)));
    }

    @Test
    // A regisztrációs űrlap beküldésének tesztelése érvényes felhasználóval
    void registerUser_validUser_shouldRedirectToLoginSuccess() throws Exception {
        when(userService.registerNewUser(any(User.class))).thenReturn(new User());
        mockMvc.perform(post("/register")
                        .flashAttr("user", testUser)
                        .param("confirmPassword", "password123"))
                // Ellenőrzi, hogy átirányít-e
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered=true"));
    }

    @Test
    // A regisztrációs űrlap beküldésének tesztelése érvénytelen felhasználóval
    void registerUser_usernameTaken_shouldReturnRegisterViewWithError() throws Exception {
        String errorMessage = "A felhasználónév már foglalt.";
        doThrow(new RuntimeException(errorMessage))
                .when(userService).registerNewUser(any(User.class));
        mockMvc.perform(post("/register")
                        .flashAttr("user", testUser)
                        .param("confirmPassword", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", is(errorMessage)));
    }

    @Test
    // A bejelentkezési űrlap megjelenítésének tesztelése különböző paraméterekkel
    void showLoginForm_noParams_shouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeDoesNotExist("errorMessage"))
                .andExpect(model().attributeDoesNotExist("message"));
    }

    @Test
    // A bejelentkezési űrlap megjelenítésének tesztelése hibával
    void showLoginForm_withErrorParam_shouldReturnLoginViewWithError() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", is("Hibás felhasználónév vagy jelszó.")));
    }

    @Test
    // A bejelentkezési űrlap megjelenítésének tesztelése kijelentkezési paraméterrel
    void showLoginForm_withLogoutParam_shouldReturnLoginViewWithMessage() throws Exception {
        mockMvc.perform(get("/login").param("logout", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", is("Sikeresen kijelentkezett.")));
    }

    @Test
    // A bejelentkezési űrlap megjelenítésének tesztelése regisztrációs paraméterrel
    void showLoginForm_withRegisteredParam_shouldReturnLoginViewWithMessage() throws Exception {
        mockMvc.perform(get("/login").param("registered", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", is("Sikeres regisztráció! Kérjük, jelentkezzen be.")));
    }
}