package com.example.bookshopweb.repository;

import com.example.bookshopweb.entity.Book;
import com.example.bookshopweb.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/* A BookRepository osztály tesztelésére szolgáló osztály
 *  Hozzá tartozó osztályok: Book, User
*/

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;
    private User userWithNoBooks;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        userRepository.deleteAll();
        // Felhasználók létrehozása
        user1 = new User("testuser1", "pass1");
        // Felhasználó perzisztálása (tartós tárolás)
        entityManager.persist(user1);

        user2 = new User("testuser2", "pass2");
        entityManager.persist(user2);

        // Létrehoz egy felhasználót, akihez NEM rendel könyvet
        userWithNoBooks = new User("user_without_books", "nopass");
        entityManager.persist(userWithNoBooks);

        // Az entitások tényleges perzisztálása az adatbázisba
        entityManager.flush();
        entityManager.clear();

        // User objektumok újra betöltése, mert az előző clear() leválasztotta őket.
        user1 = entityManager.find(User.class, user1.getId());
        user2 = entityManager.find(User.class, user2.getId());
        userWithNoBooks = entityManager.find(User.class, userWithNoBooks.getId()); // Ezt is újra betöltjük

        // Könyvek létrehozása és perzisztálása, most már
        // a kezelt felhasználói objektumokkal
        book1 = new Book("Title1", "Author1", "ISBN1", "Desc1", "url1", 10.0);
        book1.setUser(user1);
        entityManager.persist(book1);

        book2 = new Book("Title2", "Author2", "ISBN2", "Desc2", "url2", 20.0);
        book2.setUser(user1);
        entityManager.persist(book2);

        book3 = new Book("Title3", "Author3", "ISBN3", "Desc3", "url3", 30.0);
        book3.setUser(user2);
        entityManager.persist(book3);

        entityManager.flush();
    }

    @Test
    // A felhasználó által létrehozott könyvek lekérése
    void testFindByUser() {
        List<Book> booksFoundByUser1 = bookRepository.findByUser(user1);
        List<Book> booksFoundByUser2 = bookRepository.findByUser(user2);
        List<Book> booksFoundByUserWithNoBooks = bookRepository.findByUser(userWithNoBooks);

        // Ellenőrizzük, hogy a lekért könyvek listája helyes-e
        assertThat(booksFoundByUser1).isNotNull();
        assertThat(booksFoundByUser1).hasSize(2);
        assertThat(booksFoundByUser1).containsExactlyInAnyOrder(book1, book2);
        assertThat(booksFoundByUser1).doesNotContain(book3);

        assertThat(booksFoundByUser2).isNotNull();
        assertThat(booksFoundByUser2).hasSize(1);
        assertThat(booksFoundByUser2).containsExactly(book3);

        assertThat(booksFoundByUserWithNoBooks).isNotNull();
        assertThat(booksFoundByUserWithNoBooks).isEmpty(); // Ennek a listának üresnek kell lennie
    }

    @Test
    // Teszteli, hogy egy új könyv sikeresen elmenthető-e
    void testSaveBook() {
        User newUser = new User("new_saver_user", "new_pass");
        entityManager.persist(newUser);
        entityManager.flush();
        entityManager.clear();
        newUser = entityManager.find(User.class, newUser.getId());

        // Új könyv létrehozása és elmentése + felhasználó hozzárendelése
        Book newBook = new Book("New Book", "New Author", "New ISBN", "New Desc", "NewUrl", 50.0);
        newBook.setUser(newUser);

        // Könyv elmentése a repository-ba
        Book savedBook = bookRepository.save(newBook);
        entityManager.flush();
        entityManager.clear();

        // Ellenőrizzük, hogy a könyv sikeresen elmentődött-e
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isNotNull();
        // Könyv keresése az ID alapján
        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());
        // Ellenőrizzük, hogy a könyv megtalálható-e
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("New Book");
        assertThat(foundBook.get().getUser().getUsername()).isEqualTo("new_saver_user");
    }

    @Test
    // Teszteli, hogy egy könyv sikeresen lekérdezhető-e az ID alapján
    void testFindById() {
        Optional<Book> foundBook1 = bookRepository.findById(book1.getId());
        Optional<Book> foundNonExistentBook = bookRepository.findById(999L);

        assertThat(foundBook1).isPresent();
        assertThat(foundBook1.get().getTitle()).isEqualTo("Title1");
        assertThat(foundBook1.get().getUser().getUsername()).isEqualTo("testuser1");

        assertThat(foundNonExistentBook).isNotPresent();
    }

    @Test
    // Teszteli, hogy az összes könyv sikeresen lekérdezhető-e
    void testFindAll() {
        List<Book> allBooks = bookRepository.findAll();

        assertThat(allBooks).isNotNull();
        assertThat(allBooks).hasSize(3);
        assertThat(allBooks).containsExactlyInAnyOrder(book1, book2, book3);
    }

    @Test
    // Teszteli, hogy egy könyv sikeresen törölhető-e
    void testDeleteBook() {
        Long bookIdToDelete = book1.getId();

        bookRepository.deleteById(bookIdToDelete);
        entityManager.flush();
        entityManager.clear();

        Optional<Book> deletedBook = bookRepository.findById(bookIdToDelete);
        assertThat(deletedBook).isNotPresent();
        assertThat(bookRepository.findAll()).hasSize(2);
    }

    @Test
    // Teszteli, hogy a könyvek száma helyesen számolható-e
    void testCountBooks() {
        long count = bookRepository.count();
        assertThat(count).isEqualTo(3);
    }
}