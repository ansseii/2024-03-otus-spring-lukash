package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Genre;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaGenreRepositoryTest {

    private static final Long EXISTING_GENRE_1 = 1L;
    private static final Long EXISTING_GENRE_2 = 2L;
    private static final Long EXISTING_GENRE_3 = 3L;
    private static final Long EXISTING_GENRE_4 = 4L;
    private static final Long EXISTING_GENRE_5 = 5L;
    private static final Long EXISTING_GENRE_6 = 6L;

    private static final Long NONEXISTENT_GENRE_1 = 101L;
    private static final Long NONEXISTENT_GENRE_2 = 102L;
    private static final Long NONEXISTENT_GENRE_3 = 103L;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Test find all genres")
    void givenGenres_whenFindAll_thenReturnAllGenres() {
        var genreOne = entityManager.find(Genre.class, EXISTING_GENRE_1);
        var genreTwo = entityManager.find(Genre.class, EXISTING_GENRE_2);
        var genreThree = entityManager.find(Genre.class, EXISTING_GENRE_3);
        var genreFour = entityManager.find(Genre.class, EXISTING_GENRE_4);
        var genreFive = entityManager.find(Genre.class, EXISTING_GENRE_5);
        var genreSix = entityManager.find(Genre.class, EXISTING_GENRE_6);

        var actual = genreRepository.findAll();

        assertThat(actual).containsExactly(genreOne, genreTwo, genreThree, genreFour, genreFive, genreSix);
    }

    @Test
    @DisplayName("Test find all genres by provided ids")
    void givenGenresIds_whenFindAllByIds_thenReturnFoundGenres() {
        var genreOne = entityManager.find(Genre.class, EXISTING_GENRE_1);
        var genreTwo = entityManager.find(Genre.class, EXISTING_GENRE_2);
        var genreThree = entityManager.find(Genre.class, EXISTING_GENRE_3);

        var actual = genreRepository.findAllByIdIn(Set.of(
                EXISTING_GENRE_1, EXISTING_GENRE_2, EXISTING_GENRE_3,
                NONEXISTENT_GENRE_1, NONEXISTENT_GENRE_2, NONEXISTENT_GENRE_3
        ));

        assertThat(actual).containsExactly(genreOne, genreTwo, genreThree);
    }
}