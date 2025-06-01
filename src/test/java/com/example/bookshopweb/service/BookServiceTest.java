package com.example.bookshopweb.service;

import com.example.bookshopweb.entity.Book;
import com.example.bookshopweb.entity.User;
import com.example.bookshopweb.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/* A BookService osztály teszteléséhez szükséges tesztosztály
 *  Ez az osztály a könyvek kezeléséhez szükséges szolgáltatások
 *  tesztelésére szolgál.
 *  Hozzá tartozó osztályok: BookService, BookRepository, Book, User
*/

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book1;
    private Book book2;
    private User user1;

    @BeforeEach
    // A teszt környezet előkészítése,
    // itt hozza létre a mockolt objektumokat
    void setUp() {
        user1 = new User("testuser", "pass", "ROLE_USER");
        user1.setId(1L);
        book1 = new Book("Title A", "Author X", "ISBN-A", "Desc A", "urlA", 10.0);
        book1.setId(1L);
        book1.setUser(user1); // Fontos, hogy a user be legyen állítva, ha ilyen a Book entitásod

        book2 = new Book("Title B", "Author Y", "ISBN-B", "Desc B", "urlB", 20.0);
        book2.setId(2L);
        book2.setUser(user1);
    }

    @Test
    @DisplayName("Should return all books")
    // Ellenőrzi, hogy a getAllBooks() metódus helyesen működik-e
    // és visszaadja-e az összes könyvet a mockolt repository-ból.
    void getAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));
        // Vissza kell adnia a mockolt listát
        List<Book> books = bookService.getAllBooks();

        assertThat(books).isNotNull();
        assertThat(books).hasSize(2);
        assertThat(books).containsExactlyInAnyOrder(book1, book2);

        // Ellenőrzi, hogy a findAll() metódus hívva lett-e a repository-n
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return book by ID")
    // Ellenőrzi, hogy a getBookById() metódus helyesen működik-e
    void getBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        // Vissza kell adnia a mockolt könyvet
        Optional<Book> foundBook = bookService.getBookById(1L);

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("Title A");

        // Ellenőrizzük, hogy a findById() metódus hívva lett-e
        // a repository-n az 1-es ID-vel
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty optional when book not found by ID")
    // Ellenőrzi, hogy a getBookById() metódus helyesen működik-e
    void getBookByIdNotFound() {
        // Egy nem létező ID-vel
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        // Üres Optional-t kell visszaadnia
        Optional<Book> foundBook = bookService.getBookById(99L);

        assertThat(foundBook).isNotPresent();

        verify(bookRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Should save a book")
    // Ellenőrzi, hogy a saveBook() metódus helyesen működik-e
    // és elmenti-e az új könyvet a mockolt repository-
    void saveBook() {
        // Egy új könyv hozzáadása
        Book newBook = new Book("New Title", "New Author", "New ISBN", "New Desc", "NewUrl", 30.0);
        bookService.saveBook(newBook);
        // Ellenőrzi, hogy a bookRepository.save() hívva lett-e az új könyvvel
        verify(bookRepository, times(1)).save(newBook);
    }

    @Test
    @DisplayName("Should delete a book by ID")
    // Ellenőrzi, hogy a deleteBook() metódus helyesen működik-e
    void deleteBook() {
        bookService.deleteBook(1L);
        // Ellenőrzi, hogy a bookRepository.deleteById() hívva lett-e az ID-vel
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should save a book with a specific user")
    // Ellenőrzi, hogy a saveBook(Book, User) metódus helyesen működik-e
    void saveBookWithUser() {
        // Egy új könyv és egy felhasználó
        Book bookToSave = new Book("User Book", "User Author", "User ISBN", "User Desc", "User Url", 40.0);
        User specificUser = new User("specificuser", "pass", "ROLE_USER");
        specificUser.setId(2L);
        bookService.saveBook(bookToSave, specificUser);
        // Ellenőrzi, hogy a könyvhöz hozzá lett-e rendelve a felhasználó
        assertThat(bookToSave.getUser()).isEqualTo(specificUser);
        // Ellenőrzi, hogy a bookRepository.save() hívva lett-e a módosított könyvvel
        verify(bookRepository, times(1)).save(bookToSave);
    }

    @Test
    @DisplayName("Should return books by user")
    // Ellenőrzi, hogy a getBooksByUser() metódus helyesen működik-e
    void getBooksByUser() {
        // Egy felhasználó és a hozzá tartozó könyvek listája
        User queriedUser = new User("queried_user", "pass", "ROLE_USER");
        queriedUser.setId(3L);
        Book bookForUser = new Book("User Specific Book", "User Author", "ISBN-U", "Desc U", "urlU", 50.0);
        bookForUser.setId(3L);
        bookForUser.setUser(queriedUser);

        when(bookRepository.findByUser(queriedUser)).thenReturn(Arrays.asList(bookForUser));
        // A bookService.getBooksByUser() metódusnak vissza kell adnia
        // a mockolt listát
        List<Book> books = bookService.getBooksByUser(queriedUser);

        // Ellenőrzi, hogy a visszaadott lista nem null
        // és tartalmazza a könyvet
        assertThat(books).isNotNull();
        assertThat(books).hasSize(1);
        assertThat(books).containsExactly(bookForUser);

        // Ellenőrizzük, hogy a findByUser() metódus hívva lett-e a repository-n
        verify(bookRepository, times(1)).findByUser(queriedUser);
    }
}