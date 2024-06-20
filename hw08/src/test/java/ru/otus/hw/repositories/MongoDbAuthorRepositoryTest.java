package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;


@DataMongoTest
class MongoDbAuthorRepositoryTest {

    private static final String EXISTING_AUTHOR_ID_1 = "1";
    private static final String EXISTING_AUTHOR_ID_2 = "2";
    private static final String EXISTING_AUTHOR_ID_3 = "3";

    private static final String NONEXISTENT_AUTHOR_ID = "100";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    @DisplayName("Test find all authors")
    void givenAuthors_whenFindAll_thenReturnAllAuthors() {
        var authorOne = mongoOperations.findById(EXISTING_AUTHOR_ID_1, Author.class);
        var authorTwo = mongoOperations.findById(EXISTING_AUTHOR_ID_2, Author.class);
        var authorThree = mongoOperations.findById(EXISTING_AUTHOR_ID_3, Author.class);

        var actualAuthors = authorRepository.findAll();

        assertThat(actualAuthors).containsExactly(authorOne, authorTwo, authorThree);
    }

    @Test
    @DisplayName("Test find existing author by id")
    void givenId_whenFindById_thenReturnAuthor() {
        var expected = mongoOperations.findById(EXISTING_AUTHOR_ID_1, Author.class);
        var actual = authorRepository.findById(EXISTING_AUTHOR_ID_1);

        assertThat(actual).isPresent()
                .hasValue(expected);
    }

    @Test
    @DisplayName("Test find nonexistent author by id")
    void givenIncorrectId_whenFindById_thenReturnEmpty() {
        var actual = authorRepository.findById(NONEXISTENT_AUTHOR_ID);

        assertThat(actual).isNotPresent();
    }
}