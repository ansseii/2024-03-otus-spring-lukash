package ru.otus.hw.it;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional(propagation = Propagation.NEVER)
@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
class BookServiceIT {

    private static final Long EXISTING_BOOK_ID = 1L;
    private static final Long NONEXISTENT_BOOK_ID = 4L;

    @Autowired
    private BookService bookService;

    @Test
    @DisplayName("Test find existing book by id")
    void givenBookId_whenFindById_thenReturnBook() {
        var expected = getAllBooks().get(0);
        var actual = bookService.findById(EXISTING_BOOK_ID);

        assertThat(actual).isPresent()
                .hasValue(expected);
    }

    @Test
    @DisplayName("Test find nonexistent book by id")
    void givenIncorrectBookId_whenFindById_thenReturnEmpty() {
        var actual = bookService.findById(NONEXISTENT_BOOK_ID);

        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("Test find all books")
    void givenBookId_whenFindAll_thenReturnAllBooks() {
        var expected = getAllBooks();
        var actual = bookService.findAll();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DirtiesContext
    @DisplayName("Test create new book")
    void givenNewBook_whenInsert_thenReturnCreatedBook() {
        var expected = new BookDto(NONEXISTENT_BOOK_ID, "New book created title",
                new AuthorDto(1L, "Author_1"),
                List.of(
                        new GenreDto(1L, "Genre_1"),
                        new GenreDto(2L, "Genre_2")
                ));

        assertThat(bookService.findAll()).hasSize(3);

        bookService.insert("New book created title", 1L, Set.of(1L, 2L));
        assertThat(bookService.findAll()).hasSize(4);

        var actual = bookService.findById(NONEXISTENT_BOOK_ID);
        assertThat(actual).isPresent()
                .hasValue(expected);
    }

    @Test
    @DirtiesContext
    @DisplayName("Test update existing book")
    void givenExistingBook_whenUpdate_thenReturnUpdatedBook() {
        var expected = new BookDto(EXISTING_BOOK_ID, "Existing book updated title",
                new AuthorDto(1L, "Author_1"),
                List.of(
                        new GenreDto(1L, "Genre_1"),
                        new GenreDto(2L, "Genre_2")
                ));

        assertThat(bookService.findAll()).hasSize(3);

        bookService.update(EXISTING_BOOK_ID, "Existing book updated title", 1L, Set.of(1L, 2L));
        assertThat(bookService.findAll()).hasSize(3);

        var actual = bookService.findById(EXISTING_BOOK_ID);
        assertThat(actual).isPresent()
                .hasValue(expected);
    }

    @Test
    @DirtiesContext
    @DisplayName("Test delete existing book")
    void givenExistingBook_whenDelete_thenDeleteBook() {
        assertThat(bookService.findAll()).hasSize(3);

        bookService.deleteById(EXISTING_BOOK_ID);
        assertThat(bookService.findAll()).hasSize(2);
        assertThat(bookService.findById(EXISTING_BOOK_ID)).isEmpty();
    }

    private List<BookDto> getAllBooks() {
        return List.of(
                new BookDto(1L, "BookTitle_1",
                        new AuthorDto(1L, "Author_1"),
                        List.of(
                                new GenreDto(1L, "Genre_1"),
                                new GenreDto(2L, "Genre_2")
                        )),
                new BookDto(2L, "BookTitle_2",
                        new AuthorDto(2L, "Author_2"),
                        List.of(
                                new GenreDto(3L, "Genre_3"),
                                new GenreDto(4L, "Genre_4")
                        )),
                new BookDto(3L, "BookTitle_3",
                        new AuthorDto(3L, "Author_3"),
                        List.of(
                                new GenreDto(5L, "Genre_5"),
                                new GenreDto(6L, "Genre_6")
                        ))
        );
    }
}
