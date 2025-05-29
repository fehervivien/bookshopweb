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

//A UserService és a UserServiceImpl osztályok tesztelése

@ExtendWith(MockitoExtension.class) // Ez engedélyezi a Mockito annotációkat a JUnit 5-ben
public class UserServiceTest {

    @Mock // Mock objektum a UserRepository-ból
    private UserRepository userRepository;

    @Mock // Mock objektum a PasswordEncoder-ből
    private PasswordEncoder passwordEncoder;

    @InjectMocks // Ezzel injektáljuk a mockolt függőségeket az UserServiceImpl-be
    private UserServiceImpl userService; // Az implementációt teszteljük

    private User newUser;
    private User existingUser;
    private User userWithEncodedPass; // Egy felhasználó, ahogy az adatbázisban tárolódna

    @BeforeEach
    void setUp() {
        // Inicializálunk néhány tesztadatot
        newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("rawpassword");
        newUser.setRoles("ROLE_USER");
        // existingUser.id-t is beállítunk, mert a findByUsername gyakran visszaadja
        existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existinguser");
        existingUser.setPassword("rawpassword_existing");
        existingUser.setRoles("ROLE_ADMIN");
        // Egy "mentett" felhasználó, már kódolt jelszóval, ahogy a loadUserByUsername visszaadná
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
        // Arrange:
        // 1. userRepository.findByUsername() returns null (user does not exist)
        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(null);
        // 2. passwordEncoder.encode() returns the encoded password
        when(passwordEncoder.encode("rawpassword")).thenReturn("encoded_rawpassword");
        // 3. userRepository.save() returns the saved user (with ID)
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setId(2L); // Assign an ID upon saving
            return userToSave;
        });

        // Act:
        User registeredUser = userService.registerNewUser(newUser);

        // Assert:
        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getId()).isNotNull();
        assertThat(registeredUser.getUsername()).isEqualTo("newuser");
        assertThat(registeredUser.getPassword()).isEqualTo("encoded_rawpassword");

        // Verify that the correct methods were called
        verify(userRepository, times(1)).findByUsername(newUser.getUsername());
        verify(passwordEncoder, times(1)).encode("rawpassword");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception if username already exists during registration")
    void registerNewUser_UsernameExists() {
        // Amikor:
        // userRepository.findByUsername() visszaadja a már létező felhasználót
        when(userRepository.findByUsername(existingUser.getUsername())).thenReturn(existingUser);

        // Akkor:
        // Kivételt kell dobnia
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userService.registerNewUser(existingUser);
        });

        // Ellenőrizzük a kivétel üzenetét
        assertThat(thrown.getMessage()).contains("A felhasználónév már foglalt: " + existingUser.getUsername());

        // Ellenőrizzük, hogy a jelszókódolás és a mentés nem történt meg
        verify(userRepository, times(1)).findByUsername(existingUser.getUsername());
        verify(passwordEncoder, never()).encode(anyString()); // Fontos: a jelszókódolás nem hívódott meg
        verify(userRepository, never()).save(any(User.class)); // Fontos: a mentés sem hívódott meg
    }

    // --- findByUsername metódus tesztjei --- (változatlan a ServiceImpl-ben)

    @Test
    @DisplayName("Should find user by username successfully")
    void findByUsername_Found() {
        // Amikor:
        when(userRepository.findByUsername(existingUser.getUsername())).thenReturn(existingUser);

        // Akkor:
        User foundUser = userService.findByUsername(existingUser.getUsername());

        // Ellenőrzések:
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("existinguser");
        assertThat(foundUser.getId()).isEqualTo(1L);

        // Ellenőrizzük, hogy a findByUsername metódus hívva lett-e
        verify(userRepository, times(1)).findByUsername(existingUser.getUsername());
    }

    @Test
    @DisplayName("Should return null when user not found by username")
    void findByUsername_NotFound() {
        // Amikor:
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(null);

        // Akkor:
        User foundUser = userService.findByUsername("nonexistentuser");

        // Ellenőrzések:
        assertThat(foundUser).isNull();

        verify(userRepository, times(1)).findByUsername("nonexistentuser");
    }

    // --- loadUserByUsername metódus tesztjei (új) ---

    @Test
    @DisplayName("Should load user by username for Spring Security")
    void loadUserByUsername_Success() {
        // Amikor:
        // userRepository.findByUsername() visszaadja a kódolt jelszóval rendelkező felhasználót
        when(userRepository.findByUsername(userWithEncodedPass.getUsername())).thenReturn(userWithEncodedPass);

        // Akkor:
        UserDetails userDetails = userService.loadUserByUsername(userWithEncodedPass.getUsername());

        // Ellenőrzések:
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(userWithEncodedPass.getUsername());
        assertThat(userDetails.getPassword()).isEqualTo(userWithEncodedPass.getPassword()); // A jelszó már kódolt
        assertThat(userDetails.getAuthorities()).isEmpty(); // Az implementáció szerint Collections.emptyList()

        verify(userRepository, times(1)).findByUsername(userWithEncodedPass.getUsername());
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found for Spring Security")
    void loadUserByUsername_NotFound() {
        // Amikor:
        // userRepository.findByUsername() null-t ad vissza
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(null);

        // Akkor:
        // UsernameNotFoundException kivételt kell dobnia
        UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistentuser");
        });

        // Ellenőrizzük a kivétel üzenetét
        assertThat(thrown.getMessage()).contains("Nincs ilyen felhasználó: nonexistentuser");

        verify(userRepository, times(1)).findByUsername("nonexistentuser");
    }
}