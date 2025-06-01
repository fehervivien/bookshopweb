package com.example.bookshopweb.controller;

import com.example.bookshopweb.entity.Book;
import com.example.bookshopweb.entity.User;
import com.example.bookshopweb.service.BookService;
import com.example.bookshopweb.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration; // ÚJ IMPORT
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean; // ÚJ IMPORT
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Ezek az importok kellenek a Thymeleaf konfigurációhoz:
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

/*
* A BookController osztályt teszteli.
* Teszteli a könyvek listázását, hozzáadását, szerkesztését és törlését.
* */

// WebMvcTest: Lehetővé teszi a kontroller tesztelését anélkül,
// hogy az egész alkalmazást elindítanánk.
@WebMvcTest(controllers = BookController.class)
public class BookControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserRepository userRepository;

    private User mockUser;
    private User anotherUser;

    private Book book1;
    private Book book2;
    private Book anotherUsersBook;

    private static final String MOCK_USERNAME = "testuser";
    private static final String ANOTHER_USERNAME = "anotheruser";


    @TestConfiguration
    // Belső konfigurációs osztály, azért hogy a Thymeleaf sablonokban
    // használhassuk a Spring Security funkciókat.
    static class ThymeleafSecurityTestConfig {
        @Bean
        public SpringTemplateEngine templateEngine(SpringSecurityDialect securityDialect) {
            SpringTemplateEngine templateEngine = new SpringTemplateEngine();
            templateEngine.addDialect(securityDialect);
            return templateEngine;
        }

        @Bean
        // Spring Security funkciók használatát teszi elérhetővé a Thymeleaf sablonokban.
        public SpringSecurityDialect securityDialect() {
            return new SpringSecurityDialect();
        }
    }


    @BeforeEach
    // Minden teszt előtt lefut, beállítja a MockMvc-t és a tesztadatokat.
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername(MOCK_USERNAME);
        mockUser.setPassword("encodedpass");

        anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setUsername(ANOTHER_USERNAME);
        anotherUser.setPassword("encodedpass2");

        book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book Title 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("ISBN-001");
        book1.setDescription("Desc 1");
        book1.setCoverUrl("url1");
        book1.setPrice(10.00);
        book1.setUser(mockUser);

        book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book Title 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("ISBN-002");
        book2.setDescription("Desc 2");
        book2.setCoverUrl("url2");
        book2.setPrice(20.00);
        book2.setUser(mockUser);

        anotherUsersBook = new Book();
        anotherUsersBook.setId(3L);
        anotherUsersBook.setTitle("Another User's Book");
        anotherUsersBook.setAuthor("Author 3");
        anotherUsersBook.setIsbn("ISBN-003");
        anotherUsersBook.setDescription("Desc 3");
        anotherUsersBook.setCoverUrl("url3");
        anotherUsersBook.setPrice(30.00);
        anotherUsersBook.setUser(anotherUser);

        when(userRepository.findByUsername(MOCK_USERNAME)).thenReturn(mockUser);
        when(userRepository.findByUsername(ANOTHER_USERNAME)).thenReturn(anotherUser);
    }

// listBooks  tesztek
    @Test
    @WithMockUser(username = MOCK_USERNAME)
    // A könyvek listázására szolgáló teszt, amely ellenőrzi,
    // hogy a felhasználó saját könyveit látja.
    void listBooks_shouldReturnBookListForCurrentUser() throws Exception {
        when(bookService.getBooksByUser(mockUser)).thenReturn(Arrays.asList(book1, book2));
        mockMvc.perform(get("/books/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", hasSize(2)))
                .andExpect(model().attribute("books", is(Arrays.asList(book1, book2))));

        verify(bookService, times(1)).getBooksByUser(mockUser);
        verify(bookService, times(0)).getAllBooks();
    }

    @Test
    @WithMockUser(username = MOCK_USERNAME)
    // A könyvek listázására szolgáló teszt, amely ellenőrzi,
    // hogy a felhasználó más felhasználó könyveit nem látja.
    void listBooks_noBooksForUser_shouldReturnEmptyBookList() throws Exception {
        when(bookService.getBooksByUser(mockUser)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/books/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", hasSize(0)));

        verify(bookService, times(1)).getBooksByUser(mockUser);
    }

    @Test
    @WithMockUser(username = "nonexistentuser")
    // A könyvek listázására szolgáló teszt, amely ellenőrzi,
    // hogy ha a felhasználó nem található, akkor az összes könyvet visszaadja.
    void listBooks_userNotFoundInRepo_shouldReturnAllBooksAsFallback() throws Exception {
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(null);
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2, anotherUsersBook));
        mockMvc.perform(get("/books/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", hasSize(3)));

        verify(bookService, times(0)).getBooksByUser(any(User.class));
        verify(bookService, times(1)).getAllBooks();
    }

// AddBook tesztek
    @Test
    @WithMockUser(username = MOCK_USERNAME)
    // A könyvek listázására szolgáló teszt, amely ellenőrzi,
    // hogy ha a felhasználó nem található, akkor az üres könyvlistát adja vissza.
    void showAddBookForm_shouldReturnAddBookViewWithNewBook() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-book"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", instanceOf(Book.class)));
    }

    @Test
    @WithMockUser(username = MOCK_USERNAME)
    // A könyv hozzáadására szolgáló teszt, amely ellenőrzi,
    // hogy érvényes könyv hozzáadása esetén átirányít a könyvek listájára.
    void addBook_validBook_shouldRedirectToList() throws Exception {
        doNothing().when(bookService).saveBook(any(Book.class), eq(mockUser));
        mockMvc.perform(post("/books/add")
                        .with(csrf())
                        .flashAttr("book", book1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/list"));

        verify(bookService, times(1)).saveBook(any(Book.class), eq(mockUser));
        verify(bookService, times(0)).saveBook(any(Book.class));
    }

    @Test
    @WithMockUser(username = "nonexistentuser")
    // A könyv hozzáadására szolgáló teszt, amely ellenőrzi,
    // hogy ha a felhasználó nem található, akkor a könyvet
    // felhasználó nélkül menti el.
    void addBook_userNotFoundInRepo_shouldSaveBookWithoutUserAsFallback() throws Exception {
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(null);
        doNothing().when(bookService).saveBook(any(Book.class));
        mockMvc.perform(post("/books/add")
                        .with(csrf())
                        .flashAttr("book", book1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/list"));

        verify(bookService, times(1)).saveBook(any(Book.class));
        verify(bookService, times(0)).saveBook(any(Book.class), any(User.class));
    }

// viewBookDetails tesztek
    @Test
    @WithMockUser(username = MOCK_USERNAME)
    // A könyv hozzáadására szolgáló teszt, amely ellenőrzi,
    // hogy érvénytelen könyv hozzáadása esetén visszatér a
    // hozzáadás űrlapra hibával.
    // Érvénytelen könyv: null cím, üres szerző, stb.
    void viewBookDetails_bookFound_shouldReturnBookDetailView() throws Exception {
        when(bookService.getBookById(book1.getId())).thenReturn(Optional.of(book1));
        mockMvc.perform(get("/books/" + book1.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("book-detail"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", is(book1)));
    }

    @Test
    @WithMockUser(username = MOCK_USERNAME)
    // A könyv részleteinek megtekintésére szolgáló teszt, amely ellenőrzi,
    // hogy ha a könyv nem található, akkor hibaüzenetet ad vissza.
    void viewBookDetails_bookNotFound_shouldReturnBookDetailViewWithError() throws Exception {
        when(bookService.getBookById(999L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/books/999"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-detail"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", is("A keresett könyv nem található.")));
    }

// showEditBookForm
    @Test
    @WithMockUser(username = MOCK_USERNAME)
    // A könyv szerkesztésére szolgáló teszt, amely ellenőrzi,
    // hogy a felhasználó saját könyvét szerkesztheti-e.
    void showEditBookForm_userOwnsBook_shouldReturnEditBookView() throws Exception {
        when(bookService.getBookById(book1.getId())).thenReturn(Optional.of(book1));
        mockMvc.perform(get("/books/edit/" + book1.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-book"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", is(book1)));
    }

    @Test
    @WithMockUser(username = MOCK_USERNAME)
    // A könyv szerkesztésére szolgáló teszt, amely ellenőrzi,
    // hogy ha a felhasználó nem saját könyvét próbálja szerkeszteni,
    // akkor átirányítja a könyvek listájára.
    void showEditBookForm_userDoesNotOwnBook_shouldRedirectToList() throws Exception {
        when(bookService.getBookById(anotherUsersBook.getId())).thenReturn(Optional.of(anotherUsersBook));
        mockMvc.perform(get("/books/edit/" + anotherUsersBook.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/list"));
    }

    @Test
    @WithMockUser(username = MOCK_USERNAME)
    // A könyv szerkesztésére szolgáló teszt, amely ellenőrzi,
    // hogy ha a könyv nem található, akkor átirányítja a könyvek listájára.
    void showEditBookForm_bookNotFound_shouldRedirectToList() throws Exception {
        when(bookService.getBookById(999L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/books/edit/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/list"));
    }

// updateBook tesztek
    @Test
    @WithMockUser(username = MOCK_USERNAME)
    // A könyv frissítésére szolgáló teszt, amely ellenőrzi,
    // hogy a felhasználó saját könyvét frissítheti-e.
    void updateBook_userOwnsBook_shouldUpdateAndRedirectToList() throws Exception {
        Book updatedFormDataBook = new Book();
        updatedFormDataBook.setTitle("Updated Title");
        updatedFormDataBook.setAuthor("Updated Author");
        updatedFormDataBook.setIsbn("UPDATED-ISBN");
        updatedFormDataBook.setDescription("Updated Description");
        updatedFormDataBook.setCoverUrl("updated_url");
        updatedFormDataBook.setPrice(99.99);

        when(bookService.getBookById(book1.getId())).thenReturn(Optional.of(book1));
        doNothing().when(bookService).saveBook(any(Book.class));

        mockMvc.perform(post("/books/update/" + book1.getId())
                        .with(csrf())
                        .flashAttr("updatedBook", updatedFormDataBook))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/list"));

        verify(bookService, times(1)).saveBook(any(Book.class));
    }

    @Test
    @WithMockUser(username = MOCK_USERNAME)
    // A könyv frissítésére szolgáló teszt, amely ellenőrzi,
    // hogy ha a felhasználó nem saját könyvét próbálja frissíteni,
    // akkor átirányítja a könyvek listájára.
    void updateBook_userDoesNotOwnBook_shouldRedirectToList() throws Exception {
        when(bookService.getBookById(anotherUsersBook.getId())).thenReturn(Optional.of(anotherUsersBook));
        mockMvc.perform(post("/books/update/" + anotherUsersBook.getId())
                        .with(csrf())
                        .flashAttr("updatedBook", new Book()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/list"));

        verify(bookService, times(0)).saveBook(any(Book.class));
    }

    @Test
    @WithMockUser(username = MOCK_USERNAME)
    // A könyv frissítésére szolgáló teszt, amely ellenőrzi,
    // hogy ha a könyv nem található, akkor átirányítja a könyvek listájára.
    void updateBook_bookNotFound_shouldRedirectToList() throws Exception {
        when(bookService.getBookById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/books/update/999")
                        .with(csrf())
                        .flashAttr("updatedBook", new Book()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/list"));

        verify(bookService, times(0)).saveBook(any(Book.class));
    }

// deleteBook tesztek
    @Test
    @WithMockUser(username = MOCK_USERNAME)
    // A könyv törlésére szolgáló teszt, amely ellenőrzi,
    // hogy a felhasználó saját könyvét törölheti-e.
    void deleteBook_userOwnsBook_shouldDeleteAndRedirectToList() throws Exception {
        when(bookService.getBookById(book1.getId())).thenReturn(Optional.of(book1));
        doNothing().when(bookService).deleteBook(book1.getId());
        mockMvc.perform(post("/books/delete/" + book1.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/list"));

        verify(bookService, times(1)).deleteBook(book1.getId());
    }

    @Test
    @WithMockUser(username = MOCK_USERNAME)
    // A könyv törlésére szolgáló teszt, amely ellenőrzi,
    // hogy ha a felhasználó nem saját könyvét próbálja törölni,
    // akkor átirányítja a könyvek listájára anélkül, hogy törölné.
    void deleteBook_userDoesNotOwnBook_shouldRedirectToListWithoutDeleting() throws Exception {
        when(bookService.getBookById(anotherUsersBook.getId())).thenReturn(Optional.of(anotherUsersBook));

        mockMvc.perform(post("/books/delete/" + anotherUsersBook.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/list"));

        verify(bookService, times(0)).deleteBook(anyLong());
    }

    @Test
    @WithMockUser(username = MOCK_USERNAME)
    // A könyv törlésére szolgáló teszt, amely ellenőrzi,
    // hogy ha a könyv nem található, akkor átirányítja
    // a könyvek listájára anélkül, hogy törölné.
    void deleteBook_bookNotFound_shouldRedirectToListWithoutDeleting() throws Exception {
        when(bookService.getBookById(999L)).thenReturn(Optional.empty());
        mockMvc.perform(post("/books/delete/999")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/list"));

        verify(bookService, times(0)).deleteBook(anyLong());
    }
}