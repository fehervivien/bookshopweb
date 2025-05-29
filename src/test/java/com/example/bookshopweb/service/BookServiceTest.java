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

@ExtendWith(MockitoExtension.class) // Ez engedélyezi a Mockito annotációkat a JUnit 5-ben
public class BookServiceTest {

    @Mock // Ezzel mock objektumot hozunk létre a BookRepository-ból
    private BookRepository bookRepository;

    @InjectMocks // Ezzel injektáljuk a mockolt BookRepository-t a BookService példányba
    private BookService bookService;

    private Book book1;
    private Book book2;
    private User user1;

    @BeforeEach
    void setUp() {
        // Inicializálunk néhány tesztadatot
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
    void getAllBooks() {
        // Amikor: A bookRepository.findAll() metódus hívásakor
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        // Akkor: A bookService.getAllBooks() metódusnak vissza kell adnia a mockolt listát
        List<Book> books = bookService.getAllBooks();

        assertThat(books).isNotNull();
        assertThat(books).hasSize(2);
        assertThat(books).containsExactlyInAnyOrder(book1, book2);

        // Ellenőrizzük, hogy a findAll() metódus hívva lett-e a repository-n
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return book by ID")
    void getBookById() {
        // Amikor: A bookRepository.findById() metódus hívásakor az 1-es ID-vel
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        // Akkor: A bookService.getBookById() metódusnak vissza kell adnia a mockolt könyvet
        Optional<Book> foundBook = bookService.getBookById(1L);

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("Title A");

        // Ellenőrizzük, hogy a findById() metódus hívva lett-e a repository-n az 1-es ID-vel
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty optional when book not found by ID")
    void getBookByIdNotFound() {
        // Amikor: A bookRepository.findById() metódus hívásakor egy nem létező ID-vel
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // Akkor: A bookService.getBookById() metódusnak üres Optional-t kell visszaadnia
        Optional<Book> foundBook = bookService.getBookById(99L);

        assertThat(foundBook).isNotPresent();

        verify(bookRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Should save a book")
    void saveBook() {
        // Adott: Egy új könyv
        Book newBook = new Book("New Title", "New Author", "New ISBN", "New Desc", "NewUrl", 30.0);

        // Amikor: A saveBook() metódus hívása
        bookService.saveBook(newBook);

        // Akkor: Ellenőrizzük, hogy a bookRepository.save() hívva lett-e az új könyvvel
        // (A Mockito nem ad vissza semmit alapértelmezetten void metódusoknál,
        //  de ellenőrizni tudjuk, hogy hívva lett-e.)
        verify(bookRepository, times(1)).save(newBook);
    }

    @Test
    @DisplayName("Should delete a book by ID")
    void deleteBook() {
        // Amikor: A deleteBook() metódus hívása
        bookService.deleteBook(1L);

        // Akkor: Ellenőrizzük, hogy a bookRepository.deleteById() hívva lett-e az ID-vel
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should save a book with a specific user")
    void saveBookWithUser() {
        // Adott: Egy új könyv és egy felhasználó
        Book bookToSave = new Book("User Book", "User Author", "User ISBN", "User Desc", "User Url", 40.0);
        User specificUser = new User("specificuser", "pass", "ROLE_USER");
        specificUser.setId(2L);

        // Amikor: A saveBook(Book, User) metódus hívása
        bookService.saveBook(bookToSave, specificUser);

        // Akkor: Ellenőrizzük, hogy a könyvhöz hozzá lett-e rendelve a felhasználó
        assertThat(bookToSave.getUser()).isEqualTo(specificUser);
        // És ellenőrizzük, hogy a bookRepository.save() hívva lett-e a módosított könyvvel
        verify(bookRepository, times(1)).save(bookToSave);
    }

    @Test
    @DisplayName("Should return books by user")
    void getBooksByUser() {
        // Adott: Egy felhasználó és a hozzá tartozó könyvek listája
        User queriedUser = new User("queried_user", "pass", "ROLE_USER");
        queriedUser.setId(3L);
        Book bookForUser = new Book("User Specific Book", "User Author", "ISBN-U", "Desc U", "urlU", 50.0);
        bookForUser.setId(3L);
        bookForUser.setUser(queriedUser);

        // Amikor: A bookRepository.findByUser() metódus hívásakor
        when(bookRepository.findByUser(queriedUser)).thenReturn(Arrays.asList(bookForUser));

        // Akkor: A bookService.getBooksByUser() metódusnak vissza kell adnia a mockolt listát
        List<Book> books = bookService.getBooksByUser(queriedUser);

        assertThat(books).isNotNull();
        assertThat(books).hasSize(1);
        assertThat(books).containsExactly(bookForUser);

        // Ellenőrizzük, hogy a findByUser() metódus hívva lett-e a repository-n
        verify(bookRepository, times(1)).findByUser(queriedUser);
    }
}