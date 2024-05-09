package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcAuthorRepository.class)
class JdbcAuthorRepositoryTest {

    @Autowired
    private JdbcAuthorRepository authorRepository;

    @Test
    void shouldFindAllAuthors() {
        var actual = authorRepository.findAll();
        assertThat(actual).isEqualTo(getDbAuthors());
    }

    @Test
    void shouldFindAuthorById() {
        var actual = authorRepository.findById(3L);
        assertThat(actual).contains(getDbAuthors().get(2));
    }

    @Test
    void shouldNotFindAuthorByWrongId() {
        var actual = authorRepository.findById(100L);
        assertThat(actual).isEmpty();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }
}
