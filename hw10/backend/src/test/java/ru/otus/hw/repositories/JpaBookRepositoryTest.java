package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaBookRepositoryTest {

    private static final Long EXISTING_GENRE_ID = 1L;
    private static final Long EXISTING_GENRE_ID_TO_UPDATE = 3L;
    private static final Long EXISTING_AUTHOR_ID = 1L;

    private static final Long EXISTING_BOOK_ID_1 = 1L;
    private static final Long EXISTING_BOOK_ID_2 = 2L;
    private static final Long EXISTING_BOOK_ID_3 = 3L;

    private static final Long NONEXISTENT_BOOK_ID_1 = 101L;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Test find existing book by id")
    void givenId_whenFindById_thenReturnBook() {
        var expected = entityManager.find(Book.class, EXISTING_BOOK_ID_1);
        var actual = bookRepository.findById(EXISTING_BOOK_ID_1);

        assertThat(actual).isPresent()
                .hasValue(expected);
    }

    @Test
    @DisplayName("Test find nonexistent book by id")
    void givenIncorrectId_whenFindById_thenReturnEmpty() {
        var actual = bookRepository.findById(NONEXISTENT_BOOK_ID_1);

        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("Test find all books")
    void givenBooks_whenFindAll_thenReturnAllBooks() {
        var bookOne = entityManager.find(Book.class, EXISTING_BOOK_ID_1);
        var bookTwo = entityManager.find(Book.class, EXISTING_BOOK_ID_2);
        var bookThree = entityManager.find(Book.class, EXISTING_BOOK_ID_3);

        var actual = bookRepository.findAll();

        assertThat(actual).containsExactly(bookOne, bookTwo, bookThree);
    }

    @Test
    @DisplayName("Test save new book")
    void givenNewBook_whenSave_thenSaveNewBook() {
        var author = entityManager.find(Author.class, EXISTING_AUTHOR_ID);
        var genre = entityManager.find(Genre.class, EXISTING_GENRE_ID);
        var bookToSave = new Book(null, "NEW_BOOK_TITLE", author, List.of(genre));

        var savedBook = bookRepository.save(bookToSave);
        assertThat(savedBook).isNotNull()
                .matches(b -> b.getId() != null)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(bookToSave);

        var foundBook = entityManager.find(Book.class, savedBook.getId());
        assertThat(foundBook).isEqualTo(savedBook);
    }

    @Test
    @DisplayName("Test update existing book")
    void givenExistingBook_whenSave_thenUpdateExistingBook() {
        var updatedTitle = "UPDATED_BOOK_TITLE";
        var existingBook = entityManager.find(Book.class, EXISTING_BOOK_ID_1);
        var additionalGenre = entityManager.find(Genre.class, EXISTING_GENRE_ID_TO_UPDATE);

        var bookToUpdate = new Book(existingBook.getId(), updatedTitle, existingBook.getAuthor(), List.of(additionalGenre));
        Book updated = bookRepository.save(bookToUpdate);

        assertThat(updated).isNotNull()
                .matches(b -> b.getTitle().equals(updatedTitle))
                .matches(b -> b.getGenres().size() == 1)
                .matches(b -> b.getGenres().contains(additionalGenre));

        assertThat(entityManager.find(Book.class, updated.getId())).isEqualTo(updated);
    }

    @Test
    @DisplayName("Test delete book")
    void givenBookId_whenDeleteById_thenDeleteBook() {
        var bookToDelete = entityManager.find(Book.class, EXISTING_BOOK_ID_1);
        assertThat(bookToDelete).isNotNull();

        bookRepository.deleteById(EXISTING_BOOK_ID_1);
        assertThat(entityManager.find(Book.class, EXISTING_BOOK_ID_1)).isNull();
    }
}