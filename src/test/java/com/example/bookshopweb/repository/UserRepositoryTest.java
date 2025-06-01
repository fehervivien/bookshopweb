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

/**
 * A UserRepository tesztelésére szolgáló teszt osztály.
 * Ez az osztály ellenőrzi a felhasználók CRUD műveleteit
 * és a lekérdezéseket.
 * Hozzá tartozó osztályok: User, UserRepository
 */

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    // Törli az adatbázisban lévő felhasználókat,
    // majd létrehozza és elmenti a tesztfelhasználókat.
    void setUp() {
        userRepository.deleteAll(); // Törli az összes felhasználót az adatbázisból
        // Tesztfelhasználók létrehozása és perzisztálása
        user1 = new User("alice", "pass_alice", "ROLE_USER");
        entityManager.persist(user1);

        user2 = new User("bob", "pass_bob", "ROLE_ADMIN");
        entityManager.persist(user2);

        user3 = new User("charlie", "pass_charlie", "ROLE_EDITOR");
        entityManager.persist(user3);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    // Teszteli, hogy egy új felhasználó sikeresen elmenthető-e az adatbázisba.
    void testSaveUser() {
        User newUser = new User("diana", "pass_diana", "ROLE_GUEST");
        User savedUser = userRepository.save(newUser);
        entityManager.flush();
        entityManager.clear();

        // Ellenőrzi, hogy elmentődött és van ID-je
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("diana");

        // Ellenőrizzük, hogy megtalálható-e az ID-je alapján
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("diana");
    }

    @Test
    // Teszteli, hogy egy felhasználó ID alapján megtalálható-e az adatbázisban.
    void testFindById() {
        // Felhasználók keresése ID alapján
        Optional<User> foundUser1 = userRepository.findById(user1.getId());
        Optional<User> foundNonExistentUser = userRepository.findById(999L);

        // Ellenőrzi az eredményeket
        assertThat(foundUser1).isPresent();
        assertThat(foundUser1.get().getUsername()).isEqualTo("alice");
        assertThat(foundUser1.get().getRoles()).isEqualTo("ROLE_USER");
        assertThat(foundNonExistentUser).isNotPresent();
    }

    @Test
    // Teszteli, hogy az összes felhasználó lekérhető-e az adatbázisból.
    void testFindAllUsers() {
        // Az összes felhasználó lekérése
        List<User> allUsers = userRepository.findAll();

        // Ellenőrzi a listát
        assertThat(allUsers).isNotNull();
        assertThat(allUsers).hasSize(3);
        assertThat(allUsers).containsExactlyInAnyOrder(user1, user2, user3);
    }

    @Test
    // Teszteli, hogy egy felhasználó sikeresen törölhető-e az adatbázisból.
    void testDeleteUser() {
        // Egy felhasználó ID-je, amit törölni szeretnénk
        Long userIdToDelete = user1.getId();

        // Törli a felhasználót
        userRepository.deleteById(userIdToDelete);
        entityManager.flush();
        entityManager.clear();
        // Ellenőrzi, hogy a felhasználó már nem található-e
        Optional<User> deletedUser = userRepository.findById(userIdToDelete);
        assertThat(deletedUser).isNotPresent();
        // Már csak 2 felhasználónak kell maradnia a 3-ból
        assertThat(userRepository.findAll()).hasSize(2);
    }

    @Test
    // Teszteli, hogy a felhasználók számát helyesen
    // számolja-e meg az adatbázis.
    void testCountUsers() {
        // Megszámolja a felhasználókat
        long count = userRepository.count();
        // Ellenőrzi a számot
        assertThat(count).isEqualTo(3);
    }

    @Test
    // Teszteli, hogy a felhasználó sikeresen megtalálható-e
    // a felhasználónév alapján.
    void testFindByUsername() {
        // Felhasználó keresése felhasználónév alapján
        User foundAlice = userRepository.findByUsername("alice");
        User foundBob = userRepository.findByUsername("bob");
        User foundNonExistent = userRepository.findByUsername("zelda");

        // Ellenőrzi az eredményeket
        assertThat(foundAlice).isNotNull();
        assertThat(foundAlice.getUsername()).isEqualTo("alice");
        assertThat(foundAlice).isEqualTo(user1);
        assertThat(foundBob).isNotNull();
        assertThat(foundBob.getUsername()).isEqualTo("bob");
        assertThat(foundBob).isEqualTo(user2);
        assertThat(foundNonExistent).isNull();
    }

    @Test
    // Teszteli, hogy a felhasználónév alapján ellenőrzi-e,
    // hogy létezik-e a felhasználó az adatbázisban.
    void testExistsByUsername() {
        // Ellenőrzi, hogy létezik-e felhasználónév alapján
        boolean existsAlice = userRepository.existsByUsername("alice");
        boolean existsBob = userRepository.existsByUsername("bob");
        boolean existsNonExistent = userRepository.existsByUsername("zelda");
        // Ellenőrzi az eredményeket
        assertThat(existsAlice).isTrue();
        assertThat(existsBob).isTrue();
        assertThat(existsNonExistent).isFalse();
    }

    @Test
    // Teszteli, hogy egy létező felhasználó módosítása
    // sikeresen végrehajtható-e az adatbázisban.
    void testUpdateUser() {
        // Egy létező felhasználó módosítása
        User existingUser = entityManager.find(User.class, user1.getId());
        assertThat(existingUser).isNotNull();
        // Módosítja a felhasználó adatait
        existingUser.setUsername("alice_updated");
        existingUser.setPassword("new_secure_pass");
        existingUser.setRoles("ROLE_ADMIN,ROLE_USER");
        // Elmenti a módosított felhasználót
        User updatedUser = userRepository.save(existingUser);
        entityManager.flush();
        entityManager.clear();

        // Ellenőrzi, hogy a változások elmentődtek-e
        Optional<User> reloadedUser = userRepository.findById(user1.getId());
        assertThat(reloadedUser).isPresent();
        assertThat(reloadedUser.get().getUsername()).isEqualTo("alice_updated");
        assertThat(reloadedUser.get().getPassword()).isEqualTo("new_secure_pass");
        assertThat(reloadedUser.get().getRoles()).isEqualTo("ROLE_ADMIN,ROLE_USER");
    }
}