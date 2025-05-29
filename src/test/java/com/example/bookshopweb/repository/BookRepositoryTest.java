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
    private User userWithNoBooks; // Új felhasználó, akihez nem tartozik könyv
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        userRepository.deleteAll();

        // Felhasználók létrehozása és perzisztálása
        user1 = new User("testuser1", "pass1");
        entityManager.persist(user1);

        user2 = new User("testuser2", "pass2");
        entityManager.persist(user2);

        // Létrehozunk egy felhasználót, akihez NEM rendelünk könyvet
        userWithNoBooks = new User("user_without_books", "nopass");
        entityManager.persist(userWithNoBooks);


        // Azonnali flush és clear a felhasználók perzisztálása után,
        // majd a felhasználók újbóli betöltése, hogy Managed állapotban legyenek
        // a könyvek hozzárendelésekor.
        entityManager.flush();
        entityManager.clear();

        // User objektumok újra betöltése, mert az előző clear() leválasztotta őket.
        user1 = entityManager.find(User.class, user1.getId());
        user2 = entityManager.find(User.class, user2.getId());
        userWithNoBooks = entityManager.find(User.class, userWithNoBooks.getId()); // Ezt is újra betöltjük


        // Könyvek létrehozása és perzisztálása, most már a kezelt felhasználói objektumokkal
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
    void testFindByUser() {
        // Amikor
        List<Book> booksFoundByUser1 = bookRepository.findByUser(user1);
        List<Book> booksFoundByUser2 = bookRepository.findByUser(user2);
        // Itt most már a userWithNoBooks objektumot adjuk át, ami perzisztált (Managed)
        List<Book> booksFoundByUserWithNoBooks = bookRepository.findByUser(userWithNoBooks);

        // Akkor
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
    void testSaveBook() {
        User newUser = new User("new_saver_user", "new_pass");
        entityManager.persist(newUser);
        entityManager.flush();
        entityManager.clear();
        newUser = entityManager.find(User.class, newUser.getId());

        Book newBook = new Book("New Book", "New Author", "New ISBN", "New Desc", "NewUrl", 50.0);
        newBook.setUser(newUser);

        Book savedBook = bookRepository.save(newBook);
        entityManager.flush();
        entityManager.clear();

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isNotNull();
        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("New Book");
        assertThat(foundBook.get().getUser().getUsername()).isEqualTo("new_saver_user");
    }

    @Test
    void testFindById() {
        Optional<Book> foundBook1 = bookRepository.findById(book1.getId());
        Optional<Book> foundNonExistentBook = bookRepository.findById(999L);

        assertThat(foundBook1).isPresent();
        assertThat(foundBook1.get().getTitle()).isEqualTo("Title1");
        assertThat(foundBook1.get().getUser().getUsername()).isEqualTo("testuser1");

        assertThat(foundNonExistentBook).isNotPresent();
    }

    @Test
    void testFindAll() {
        List<Book> allBooks = bookRepository.findAll();

        assertThat(allBooks).isNotNull();
        assertThat(allBooks).hasSize(3);
        assertThat(allBooks).containsExactlyInAnyOrder(book1, book2, book3);
    }

    @Test
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
    void testCountBooks() {
        long count = bookRepository.count();
        assertThat(count).isEqualTo(3);
    }
}