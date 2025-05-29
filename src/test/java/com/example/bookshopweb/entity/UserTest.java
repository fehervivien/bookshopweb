package com.example.bookshopweb.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user1;
    private User user2;
    private User user3;
    private User userWithNullId;

    @BeforeEach
    void setUp() {
        // Inicializálunk felhasználókat a tesztekhez
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("user_one");
        user1.setPassword("pass1");
        user1.setRoles("ROLE_USER");

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("user_two");
        user2.setPassword("pass2");
        user2.setRoles("ROLE_ADMIN"); // Más szerepkör

        user3 = new User();
        user3.setId(1L); // Ugyanaz az ID, mint user1
        user3.setUsername("user_three"); // Más felhasználónév
        user3.setPassword("another_pass"); // Más jelszó
        user3.setRoles("ROLE_GUEST"); // Más szerepkör

        userWithNullId = new User(); // ID nélkül
        userWithNullId.setUsername("new_user");
        userWithNullId.setPassword("new_pass");
    }

    @Test
    void testDefaultConstructor() {
        User user = new User();
        assertNull(user.getId(), "Az ID-nek null-nak kell lennie az alapértelmezett konstruktor után.");
        assertNull(user.getUsername(), "A Username-nek null-nak kell lennie.");
        assertNull(user.getPassword(), "A Password-nek null-nak kell lennie.");
        assertNull(user.getRoles(), "A Roles-nak null-nak kell lennie.");
    }

    @Test
    void testTwoArgConstructor() {
        String username = "testuser_two_args";
        String password = "testpass_two_args";
        User user = new User(username, password);

        assertNull(user.getId(), "Az ID-nek null-nak kell lennie a 2-argumentumú konstruktor után.");
        assertEquals(username, user.getUsername(), "A Username nem egyezik a 2-argumentumú konstruktor után.");
        assertEquals(password, user.getPassword(), "A Password nem egyezik a 2-argumentumú konstruktor után.");
        assertNull(user.getRoles(), "A Roles-nak null-nak kell lennie a 2-argumentumú konstruktor után.");
    }

    @Test
    void testThreeArgConstructor() {
        String username = "testuser_three_args";
        String password = "testpass_three_args";
        String roles = "ROLE_USER,ROLE_EDITOR";
        User user = new User(username, password, roles);

        assertNull(user.getId(), "Az ID-nek null-nak kell lennie a 3-argumentumú konstruktor után.");
        assertEquals(username, user.getUsername(), "A Username nem egyezik a 3-argumentumú konstruktor után.");
        assertEquals(password, user.getPassword(), "A Password nem egyezik a 3-argumentumú konstruktor után.");
        assertEquals(roles, user.getRoles(), "A Roles nem egyezik a 3-argumentumú konstruktor után.");
    }

    @Test
    void testGettersAndSetters() {
        User user = new User();

        Long id = 5L;
        String username = "setter_test_user";
        String password = "setter_test_pass";
        String roles = "ROLE_ADMIN";

        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setRoles(roles);

        assertEquals(id, user.getId(), "Az ID getter/setter hibás.");
        assertEquals(username, user.getUsername(), "A Username getter/setter hibás.");
        assertEquals(password, user.getPassword(), "A Password getter/setter hibás.");
        assertEquals(roles, user.getRoles(), "A Roles getter/setter hibás.");
    }

    @Test
    void testEquals_SameId_ShouldBeEqual() {
        // Ugyanaz az ID, de más mezők, mégis egyenlőnek kell lennie
        assertTrue(user1.equals(user3), "Az azonos ID-jű felhasználóknak egyenlőnek kell lenniük.");
        assertEquals(user1.hashCode(), user3.hashCode(), "Az azonos ID-jű felhasználóknak azonos hashCode-dal kell rendelkezniük.");
    }

    @Test
    void testEquals_DifferentId_ShouldNotBeEqual() {
        // Különböző ID-k
        assertFalse(user1.equals(user2), "A különböző ID-jű felhasználóknak nem szabad egyenlőnek lenniük.");
        assertNotEquals(user1.hashCode(), user2.hashCode(), "A különböző ID-jű felhasználóknak különböző hashCode-dal kell rendelkezniük.");
    }

    @Test
    void testEquals_NullId_DifferentObjects_ShouldBeEqual() {
        // Két külön objektum, mindkettőnek null az ID-je
        User anotherUserWithNullId = new User();
        anotherUserWithNullId.setUsername("another_new_user");
        anotherUserWithNullId.setPassword("another_new_pass");

        // Az Objects.equals(null, null) true, tehát ezek egyenlőek lesznek az equals metódus szerint
        assertTrue(userWithNullId.equals(anotherUserWithNullId), "Két null ID-jű, de különböző objektum egyenlőnek kell, hogy legyen.");
        assertEquals(userWithNullId.hashCode(), anotherUserWithNullId.hashCode(), "Két null ID-jű objektumnak azonos hashCode-dal kell rendelkeznie.");
    }

    @Test
    void testEquals_SameObject_ShouldBeEqual() {
        assertTrue(user1.equals(user1), "Az objektumnak saját magával egyenlőnek kell lennie.");
    }

    @Test
    void testEquals_NullObject_ShouldNotBeEqual() {
        assertFalse(user1.equals(null), "Az objektumnak nem szabad null-lal egyenlőnek lennie.");
    }

    @Test
    void testEquals_DifferentClass_ShouldNotBeEqual() {
        Object obj = new Object();
        assertFalse(user1.equals(obj), "Az objektumnak nem szabad más osztályú objektummal egyenlőnek lennie.");
    }

    @Test
    void testHashCode_Consistency() {
        // hashCode konzisztencia: ha equals true, akkor hashCode-ok is egyenlőek
        assertTrue(user1.equals(user3));
        assertEquals(user1.hashCode(), user3.hashCode(), "A hashCode-nak konzisztensnek kell lennie az equals-szel.");
    }

    @Test
    void testHashCode_NullId() {
        // hashCode tesztelése null ID esetén
        User user = new User(); // ID = null
        assertEquals(Objects.hash(null), user.hashCode(), "A hashCode-nak null ID esetén is megfelelőnek kell lennie.");

        user.setId(null); // Explicit nullra állítva
        assertEquals(Objects.hash(null), user.hashCode(), "A hashCode-nak null ID esetén is megfelelőnek kell lennie, explicit állítás után is.");
    }
}