package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.DataGenerator;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookLightDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
class BookControllerTest {

    private static final long EXISTING_BOOK_ID = 1L;
    private static final long NONEXISTENT_BOOK_ID = 100L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private BookConverter bookConverter;

    @BeforeEach
    void setUp() {
        when(bookConverter.toLightDto(any())).thenCallRealMethod();
        when(bookService.findAll()).thenReturn(DataGenerator.getAllBooks());
        when(authorService.findAll()).thenReturn(DataGenerator.getAllAuthors());
        when(genreService.findAll()).thenReturn(DataGenerator.getAllGenres());
    }

    @Test
    @DisplayName("Test get all books request")
    void givenBooks_whenGetAll_thenCorrectPage() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books-view"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", contains(DataGenerator.getAllBooks().toArray())));
    }

    @Test
    @DisplayName("Test get create book page request")
    void givenBooks_whenGetCreateBookPage_thenCorrectPage() throws Exception {
        mockMvc.perform(get("/books/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-book-view"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", is(BookLightDto.empty())))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", contains(DataGenerator.getAllAuthors().toArray())))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", contains(DataGenerator.getAllGenres().toArray())));

    }

    @Test
    @DisplayName("Test get edit book page request")
    void givenCorrectBookId_whenGetEditBookPage_thenCorrectPage() throws Exception {
        var book = DataGenerator.getAllBooks().get(0);
        var bookLight = bookConverter.toLightDto(book);

        when(bookService.findById(EXISTING_BOOK_ID)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/books/{id}/edit", EXISTING_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-book-view"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", is(bookLight)))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", contains(DataGenerator.getAllAuthors().toArray())))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", contains(DataGenerator.getAllGenres().toArray())));

    }

    @Test
    @DisplayName("Test get edit book page request with incorrect book id")
    void givenIncorrectBookId_whenGetEditBookPage_thenNotFoundPage() throws Exception {
        when(bookService.findById(NONEXISTENT_BOOK_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/books/{id}/edit", NONEXISTENT_BOOK_ID))
                .andExpect(status().isNotFound())
                .andExpect(view().name("not-found-view"));
    }

    @Test
    @DisplayName("Test save new book request")
    void givenBook_whenSaveBook_thenSuccess() throws Exception {
        var toSave = new BookLightDto(null, "BookTitle_4", 3L, Set.of(1L, 2L, 3L));

        mockMvc.perform(post("/books")
                        .flashAttr("book", toSave))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books"));

        verify(bookService).insert(toSave.title(), toSave.authorId(), toSave.genreIds());
    }

    @Test
    @DisplayName("Test save new book request when validation failed")
    void givenBookWithInvalidFields_whenSaveBook_thenValidationErrors() throws Exception {
        var toSave = new BookLightDto(null, "", null, Set.of());

        mockMvc.perform(post("/books")
                        .flashAttr("book", toSave))
                .andExpect(status().isOk())
                .andExpect(view().name("create-book-view"));

        verify(bookService, never()).insert(any(), anyLong(), any());
    }

    @Test
    @DisplayName("Test update existing book request")
    void givenBook_whenUpdateBook_thenSuccess() throws Exception {
        var toUpdate = new BookLightDto(EXISTING_BOOK_ID, "BookTitle_100500", 3L, Set.of(1L, 2L, 3L));

        mockMvc.perform(put("/books/{id}", EXISTING_BOOK_ID)
                        .flashAttr("book", toUpdate))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books"));

        verify(bookService).update(EXISTING_BOOK_ID, toUpdate.title(), toUpdate.authorId(), toUpdate.genreIds());
    }

    @Test
    @DisplayName("Test update existing book request when validation failed")
    void givenBookWithInvalidFields_whenUpdateBook_thenValidationErrors() throws Exception {
        var toUpdate = new BookLightDto(EXISTING_BOOK_ID, "", null, Set.of());

        mockMvc.perform(put("/books/{id}", EXISTING_BOOK_ID)
                        .flashAttr("book", toUpdate))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-book-view"));

        verify(bookService, never()).update(anyLong(), any(), anyLong(), any());
    }

    @Test
    @DisplayName("Test delete book request")
    void givenBook_whenDeleteBook_thenSuccess() throws Exception {
        mockMvc.perform(delete("/books/{id}", EXISTING_BOOK_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books"));

        verify(bookService).deleteById(EXISTING_BOOK_ID);
    }
}