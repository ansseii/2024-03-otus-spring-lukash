package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.hw.models.Genre;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class MongoDbGenreRepositoryTest {

    private static final String EXISTING_GENRE_1 = "1";
    private static final String EXISTING_GENRE_2 = "2";
    private static final String EXISTING_GENRE_3 = "3";
    private static final String EXISTING_GENRE_4 = "4";
    private static final String EXISTING_GENRE_5 = "5";
    private static final String EXISTING_GENRE_6 = "6";

    private static final String NONEXISTENT_GENRE_1 = "101";
    private static final String NONEXISTENT_GENRE_2 = "102";
    private static final String NONEXISTENT_GENRE_3 = "103";

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    @DisplayName("Test find all genres")
    void givenGenres_whenFindAll_thenReturnAllGenres() {
        var genreOne = mongoOperations.findById(EXISTING_GENRE_1, Genre.class);
        var genreTwo = mongoOperations.findById(EXISTING_GENRE_2, Genre.class);
        var genreThree = mongoOperations.findById(EXISTING_GENRE_3, Genre.class);
        var genreFour = mongoOperations.findById(EXISTING_GENRE_4, Genre.class);
        var genreFive = mongoOperations.findById(EXISTING_GENRE_5, Genre.class);
        var genreSix = mongoOperations.findById(EXISTING_GENRE_6, Genre.class);

        var actual = genreRepository.findAll();

        assertThat(actual).containsExactly(genreOne, genreTwo, genreThree, genreFour, genreFive, genreSix);
    }

    @Test
    @DisplayName("Test find all genres by provided ids")
    void givenGenresIds_whenFindAllByIds_thenReturnFoundGenres() {
        var genreOne = mongoOperations.findById(EXISTING_GENRE_1, Genre.class);
        var genreTwo = mongoOperations.findById(EXISTING_GENRE_2, Genre.class);
        var genreThree = mongoOperations.findById(EXISTING_GENRE_3, Genre.class);

        var actual = genreRepository.findAllByIdIn(Set.of(
                EXISTING_GENRE_1, EXISTING_GENRE_2, EXISTING_GENRE_3,
                NONEXISTENT_GENRE_1, NONEXISTENT_GENRE_2, NONEXISTENT_GENRE_3
        ));

        assertThat(actual).containsExactly(genreOne, genreTwo, genreThree);
    }
}