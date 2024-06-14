package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class MongoDbBookRepositoryTest {

    private static final String EXISTING_GENRE_ID = "1";
    private static final String EXISTING_GENRE_ID_TO_UPDATE = "3";
    private static final String EXISTING_AUTHOR_ID = "1";

    private static final String EXISTING_BOOK_ID_1 = "1";
    private static final String EXISTING_BOOK_ID_2 = "2";
    private static final String EXISTING_BOOK_ID_3 = "3";

    private static final String NONEXISTENT_BOOK_ID_1 = "101";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    @DisplayName("Test find existing book by id")
    void givenId_whenFindById_thenReturnBook() {
        var expected = mongoOperations.findById(EXISTING_BOOK_ID_1, Book.class);
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
        var bookOne = mongoOperations.findById(EXISTING_BOOK_ID_1,Book.class);
        var bookTwo = mongoOperations.findById(EXISTING_BOOK_ID_2, Book.class);
        var bookThree = mongoOperations.findById(EXISTING_BOOK_ID_3, Book.class);

        var actual = bookRepository.findAll();

        assertThat(actual).containsExactly(bookOne, bookTwo, bookThree);
    }

    @Test
    @DirtiesContext
    @DisplayName("Test save new book")
    void givenNewBook_whenSave_thenSaveNewBook() {
        var author = mongoOperations.findById(EXISTING_AUTHOR_ID, Author.class);
        var genre = mongoOperations.findById(EXISTING_GENRE_ID, Genre.class);
        var bookToSave = new Book(null, "NEW_BOOK_TITLE", author, List.of(genre));

        var savedBook = bookRepository.save(bookToSave);
        assertThat(savedBook).isNotNull()
                .matches(b -> b.getId() != null)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(bookToSave);

        var foundBook = mongoOperations.findById(savedBook.getId(), Book.class);
        assertThat(foundBook).isEqualTo(savedBook);
    }

    @Test
    @DirtiesContext
    @DisplayName("Test update existing book")
    void givenExistingBook_whenSave_thenUpdateExistingBook() {
        var updatedTitle = "UPDATED_BOOK_TITLE";
        var existingBook = mongoOperations.findById(EXISTING_BOOK_ID_1, Book.class);
        var additionalGenre = mongoOperations.findById(EXISTING_GENRE_ID_TO_UPDATE, Genre.class);

        var bookToUpdate = new Book(existingBook.getId(), updatedTitle, existingBook.getAuthor(), List.of(additionalGenre));
        Book updated = bookRepository.save(bookToUpdate);

        assertThat(updated).isNotNull()
                .matches(b -> b.getTitle().equals(updatedTitle))
                .matches(b -> b.getGenres().size() == 1)
                .matches(b -> b.getGenres().contains(additionalGenre));

        assertThat(mongoOperations.findById(updated.getId(), Book.class)).isEqualTo(updated);
    }

    @Test
    @DirtiesContext
    @DisplayName("Test delete book")
    void givenBookId_whenDeleteById_thenDeleteBook() {
        var bookToDelete = mongoOperations.findById(EXISTING_BOOK_ID_1, Book.class);
        assertThat(bookToDelete).isNotNull();

        bookRepository.deleteById(EXISTING_BOOK_ID_1);
        assertThat(mongoOperations.findById(EXISTING_BOOK_ID_1, Book.class)).isNull();
    }
}