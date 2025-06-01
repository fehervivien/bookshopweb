package com.example.bookshopweb.service;

import com.example.bookshopweb.entity.User;
import com.example.bookshopweb.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails; // Spring Security UserDetails import
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Spring Security exception import
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/* A Userservice és a UserServiceImpl osztály teszteléséhez szükséges osztály
 *  Hozzá tartozó osztályok: UserServiceImpl, UserRepository,
 *  PasswordEncoder
 *  JUnit 5 és Mockito használatával
 * */

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User newUser;
    private User existingUser;
    private User userWithEncodedPass;

    @BeforeEach
    // A tesztek előkészítése, itt hozza létre a User példányokat
    void setUp() {
        newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("rawpassword");
        newUser.setRoles("ROLE_USER");
        // Egy új felhasználó, akit regisztrálni szeretnénk
        existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existinguser");
        existingUser.setPassword("rawpassword_existing");
        existingUser.setRoles("ROLE_ADMIN");
        // Egy "mentett" felhasználó, már kódolt jelszóval
        userWithEncodedPass = new User();
        userWithEncodedPass.setId(2L);
        userWithEncodedPass.setUsername("loadeduser");
        userWithEncodedPass.setPassword("encoded_loadedpassword");
        userWithEncodedPass.setRoles("ROLE_USER");
    }

    // --- registerNewUser metódus tesztjei ---
    @Test
    @DisplayName("Should register a new user successfully")
    void registerNewUser_Success() {
        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(null);
        when(passwordEncoder.encode("rawpassword")).thenReturn("encoded_rawpassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setId(2L); // Assign an ID upon saving
            return userToSave;
        });
        User registeredUser = userService.registerNewUser(newUser);

        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getId()).isNotNull();
        assertThat(registeredUser.getUsername()).isEqualTo("newuser");
        assertThat(registeredUser.getPassword()).isEqualTo("encoded_rawpassword");

        // Ellenőrzi, hogy a userRepository.findByUsername() hívva lett-e
        verify(userRepository, times(1)).findByUsername(newUser.getUsername());
        verify(passwordEncoder, times(1)).encode("rawpassword");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception if username already exists during registration")
    // Teszteli, hogy ha a felhasználónév már létezik,
    // akkor kivételt dob
    void registerNewUser_UsernameExists() {
        when(userRepository.findByUsername(existingUser.getUsername())).thenReturn(existingUser);
        // Kivételt kell dobnia
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userService.registerNewUser(existingUser);
        });
        // Ellenőrzi a kivétel üzenetét
        assertThat(thrown.getMessage()).contains("A felhasználónév már foglalt: " + existingUser.getUsername());

        // Ellenőrzi, hogy a jelszókódolás és a mentés nem történt meg
        verify(userRepository, times(1)).findByUsername(existingUser.getUsername());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    // --- findByUsername metódus tesztjei --- (változatlan a ServiceImpl-ben)
    @Test
    @DisplayName("Should find user by username successfully")
    // Teszteli, hogy a findByUsername metódus sikeresen
    // megtalálja a felhasználót
    void findByUsername_Found() {
        when(userRepository.findByUsername(existingUser.getUsername())).thenReturn(existingUser);
        User foundUser = userService.findByUsername(existingUser.getUsername());

        // Ellenőrzések:
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("existinguser");
        assertThat(foundUser.getId()).isEqualTo(1L);

        // Ellenőrzi, hogy a findByUsername metódus hívva lett-e
        verify(userRepository, times(1)).findByUsername(existingUser.getUsername());
    }

    @Test
    @DisplayName("Should return null when user not found by username")
    void findByUsername_NotFound() {
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(null);
        User foundUser = userService.findByUsername("nonexistentuser");
        // Ellenőrzések:
        assertThat(foundUser).isNull();

        verify(userRepository, times(1)).findByUsername("nonexistentuser");
    }

    // --- loadUserByUsername metódus tesztjei (új) ---
    @Test
    @DisplayName("Should load user by username for Spring Security")
    // Teszteli, hogy a loadUserByUsername metódus sikeresen
    // betölti a felhasználót a Spring Security számára
    void loadUserByUsername_Success() {
        when(userRepository.findByUsername(userWithEncodedPass.getUsername())).thenReturn(userWithEncodedPass);
        UserDetails userDetails = userService.loadUserByUsername(userWithEncodedPass.getUsername());

        // Ellenőrzések:
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(userWithEncodedPass.getUsername());
        assertThat(userDetails.getPassword()).isEqualTo(userWithEncodedPass.getPassword());
        assertThat(userDetails.getAuthorities()).isEmpty();

        verify(userRepository, times(1)).findByUsername(userWithEncodedPass.getUsername());
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found for Spring Security")
    // Teszteli, hogy ha a felhasználó nem található,
    // akkor UsernameNotFoundException kivételt dob
    void loadUserByUsername_NotFound() {
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(null);
        // UsernameNotFoundException kivételt kell dobnia
        UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistentuser");
        });

        // Ellenőrzi a kivétel üzenetét
        assertThat(thrown.getMessage()).contains("Nincs ilyen felhasználó: nonexistentuser");

        verify(userRepository, times(1)).findByUsername("nonexistentuser");
    }
}