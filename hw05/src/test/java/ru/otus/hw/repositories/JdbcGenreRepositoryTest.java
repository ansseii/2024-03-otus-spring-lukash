package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcGenreRepository.class)
class JdbcGenreRepositoryTest {

    @Autowired
    private JdbcGenreRepository repository;

    @Test
    void shouldFindAllGenres() {
        var actual = repository.findAll();
        assertThat(actual).isEqualTo(getDbGenres());
    }

    @Test
    void shouldFindAllGenresByIds() {
        var actual = repository.findAllByIds(Set.of(1L, 3L, 5L, 7L, 9L));
        assertThat(actual).isEqualTo(List.of(
                new Genre(1L, "Genre_1"),
                new Genre(3L, "Genre_3"),
                new Genre(5L, "Genre_5")
        ));
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }
}
