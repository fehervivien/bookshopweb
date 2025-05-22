package com.example.bookshopweb.controller;

import com.example.bookshopweb.entity.Book;
import com.example.bookshopweb.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;

    @Test
    void testGetAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(
                new Book("Cím 1", "Szerző 1", 10.0),
                new Book("Cím 2", "Szerző 2", 15.0)
        ));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-list"))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    void testGetBookById_Found() throws Exception {
        Long id = 1L;
        Book book = new Book("Minta könyv", "Minta szerző", 12.5);
        book.setId(id);
        when(bookService.getBookById(id)).thenReturn(book);

        mockMvc.perform(get("/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("book-detail"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void testGetBookById_NotFound() throws Exception {
        Long id = 42L;
        when(bookService.getBookById(id)).thenReturn(null);

        mockMvc.perform(get("/books/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    void testCreateBook() throws Exception {
        mockMvc.perform(post("/books")
                        .param("title", "Új könyv")
                        .param("author", "Szerző")
                        .param("price", "20.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        Mockito.verify(bookService).saveBook(Mockito.any(Book.class));
    }

    @Test
    void testUpdateBook() throws Exception {
        Long id = 1L;

        mockMvc.perform(put("/books/{id}", id)
                        .param("title", "Frissített cím")
                        .param("author", "Frissített szerző")
                        .param("price", "25.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        Mockito.verify(bookService).updateBook(Mockito.eq(id), Mockito.any(Book.class));
    }

    @Test
    void testDeleteBook() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/books/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        Mockito.verify(bookService).deleteBook(id);
    }
}
