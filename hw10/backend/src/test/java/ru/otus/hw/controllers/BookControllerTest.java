package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.DataGenerator;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookLightDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    private static final long EXISTING_BOOK_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(objectMapper.writeValueAsString(DataGenerator.getAllBooks())));
    }

    @Test
    @DisplayName("Test save new book request")
    void givenBook_whenSaveBook_thenSuccess() throws Exception {
        var toSave = new BookLightDto(null, "BookTitle_4", 3L, Set.of(1L, 2L, 3L));
        when(bookService.insert(toSave.title(), toSave.authorId(), toSave.genreIds())).thenReturn(
                new BookDto(4L, toSave.title(), DataGenerator.getAllAuthors().get(2), DataGenerator.getAllGenres().subList(0, 3))
        );

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(toSave)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/api/v1/books/4"));
    }

    @Test
    @DisplayName("Test save new book request when validation failed")
    void givenBookWithInvalidFields_whenSaveBook_thenValidationErrors() throws Exception {
        var toSave = new BookLightDto(null, "", null, Set.of());

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(toSave)))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).insert(any(), anyLong(), any());
    }

    @Test
    @DisplayName("Test update existing book request")
    void givenBook_whenUpdateBook_thenSuccess() throws Exception {
        var toUpdate = new BookLightDto(EXISTING_BOOK_ID, "BookTitle_100500", 3L, Set.of(1L, 2L, 3L));

        mockMvc.perform(put("/api/v1/books/{id}", EXISTING_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(toUpdate)))
                .andExpect(status().isOk());

        verify(bookService).update(EXISTING_BOOK_ID, toUpdate.title(), toUpdate.authorId(), toUpdate.genreIds());
    }

    @Test
    @DisplayName("Test update existing book request when validation failed")
    void givenBookWithInvalidFields_whenUpdateBook_thenValidationErrors() throws Exception {
        var toUpdate = new BookLightDto(EXISTING_BOOK_ID, "", null, Set.of());

        mockMvc.perform(put("/api/v1/books/{id}", EXISTING_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(toUpdate)))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).update(anyLong(), any(), anyLong(), any());
    }

    @Test
    @DisplayName("Test delete book request")
    void givenBook_whenDeleteBook_thenSuccess() throws Exception {
        mockMvc.perform(delete("/api/v1/books/{id}", EXISTING_BOOK_ID))
                .andExpect(status().isNoContent());

        verify(bookService).deleteById(EXISTING_BOOK_ID);
    }
}