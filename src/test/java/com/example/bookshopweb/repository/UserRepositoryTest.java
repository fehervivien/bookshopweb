package com.example.bookshopweb.repository;

import com.example.bookshopweb.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager; // Az entitások közvetlen perzisztálására a tesztekhez

    private User user1;
    private User user2;
    private User user3; // Egy harmadik felhasználó a tesztekhez

    @BeforeEach
    void setUp() {
        // Tiszta állapot biztosítása minden teszt előtt
        userRepository.deleteAll(); // Törli az összes felhasználót az adatbázisból

        // Tesztfelhasználók létrehozása és perzisztálása
        user1 = new User("alice", "pass_alice", "ROLE_USER");
        entityManager.persist(user1);

        user2 = new User("bob", "pass_bob", "ROLE_ADMIN");
        entityManager.persist(user2);

        user3 = new User("charlie", "pass_charlie", "ROLE_EDITOR");
        entityManager.persist(user3);

        entityManager.flush(); // Várakozó változások kiírása az adatbázisba
        entityManager.clear(); // A perzisztencia kontextus törlése, hogy a tesztek friss adatokkal dolgozzanak
    }

    @Test
    void testSaveUser() {
        // Adott: Egy új felhasználó
        User newUser = new User("diana", "pass_diana", "ROLE_GUEST");

        // Amikor: Elmentjük a felhasználót
        User savedUser = userRepository.save(newUser);
        entityManager.flush(); // Biztosítjuk, hogy az adatbázisba kerüljön
        entityManager.clear(); // Frissítés a lekérdezés előtt

        // Akkor: Ellenőrizzük, hogy elmentődött és van ID-je
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("diana");

        // Ellenőrizzük, hogy megtalálható-e az ID-je alapján
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("diana");
    }

    @Test
    void testFindById() {
        // Amikor: Felhasználók keresése ID alapján
        Optional<User> foundUser1 = userRepository.findById(user1.getId());
        Optional<User> foundNonExistentUser = userRepository.findById(999L); // Nem létező ID

        // Akkor: Ellenőrizzük az eredményeket
        assertThat(foundUser1).isPresent();
        assertThat(foundUser1.get().getUsername()).isEqualTo("alice");
        assertThat(foundUser1.get().getRoles()).isEqualTo("ROLE_USER");

        assertThat(foundNonExistentUser).isNotPresent(); // Nem létező felhasználónál üresnek kell lennie
    }

    @Test
    void testFindAllUsers() {
        // Amikor: Az összes felhasználó lekérése
        List<User> allUsers = userRepository.findAll();

        // Akkor: Ellenőrizzük a listát
        assertThat(allUsers).isNotNull();
        assertThat(allUsers).hasSize(3); // A setUp() metódusban 3 felhasználót hoztunk létre
        assertThat(allUsers).containsExactlyInAnyOrder(user1, user2, user3);
    }

    @Test
    void testDeleteUser() {
        // Adott: Egy felhasználó ID-je, amit törölni szeretnénk
        Long userIdToDelete = user1.getId();

        // Amikor: Töröljük a felhasználót
        userRepository.deleteById(userIdToDelete);
        entityManager.flush();
        entityManager.clear();

        // Akkor: Ellenőrizzük, hogy sikeres volt-e a törlés
        Optional<User> deletedUser = userRepository.findById(userIdToDelete);
        assertThat(deletedUser).isNotPresent(); // Már nem szabad megtalálni
        assertThat(userRepository.findAll()).hasSize(2); // Már csak 2 felhasználó kell, hogy legyen
    }

    @Test
    void testCountUsers() {
        // Amikor: Megszámoljuk a felhasználókat
        long count = userRepository.count();

        // Akkor: Ellenőrizzük a számot
        assertThat(count).isEqualTo(3);
    }

    @Test
    void testFindByUsername() {
        // Amikor: Felhasználó keresése felhasználónév alapján
        User foundAlice = userRepository.findByUsername("alice");
        User foundBob = userRepository.findByUsername("bob");
        User foundNonExistent = userRepository.findByUsername("zelda"); // Nem létező felhasználónév

        // Akkor: Ellenőrizzük az eredményeket
        assertThat(foundAlice).isNotNull();
        assertThat(foundAlice.getUsername()).isEqualTo("alice");
        assertThat(foundAlice).isEqualTo(user1); // Az equals() metódus miatt itt az ID alapján egyezik

        assertThat(foundBob).isNotNull();
        assertThat(foundBob.getUsername()).isEqualTo("bob");
        assertThat(foundBob).isEqualTo(user2);

        assertThat(foundNonExistent).isNull(); // Nem létező felhasználónál null-t kell visszaadnia
    }

    @Test
    void testExistsByUsername() {
        // Amikor: Ellenőrizzük, hogy létezik-e felhasználónév alapján
        boolean existsAlice = userRepository.existsByUsername("alice");
        boolean existsBob = userRepository.existsByUsername("bob");
        boolean existsNonExistent = userRepository.existsByUsername("zelda");

        // Akkor: Ellenőrizzük az eredményeket
        assertThat(existsAlice).isTrue();
        assertThat(existsBob).isTrue();
        assertThat(existsNonExistent).isFalse();
    }

    @Test
    void testUpdateUser() {
        // Adott: Egy létező felhasználó módosítása
        User existingUser = entityManager.find(User.class, user1.getId());
        assertThat(existingUser).isNotNull();

        existingUser.setUsername("alice_updated");
        existingUser.setPassword("new_secure_pass");
        existingUser.setRoles("ROLE_ADMIN,ROLE_USER");

        // Amikor: Elmentjük a módosított felhasználót
        User updatedUser = userRepository.save(existingUser);
        entityManager.flush();
        entityManager.clear();

        // Akkor: Ellenőrizzük, hogy a változások elmentődtek-e
        Optional<User> reloadedUser = userRepository.findById(user1.getId());
        assertThat(reloadedUser).isPresent();
        assertThat(reloadedUser.get().getUsername()).isEqualTo("alice_updated");
        assertThat(reloadedUser.get().getPassword()).isEqualTo("new_secure_pass");
        assertThat(reloadedUser.get().getRoles()).isEqualTo("ROLE_ADMIN,ROLE_USER");
    }
}