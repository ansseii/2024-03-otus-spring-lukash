package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class JpaAuthorRepositoryTest {

    private static final Long EXISTING_AUTHOR_ID_1 = 1L;
    private static final Long EXISTING_AUTHOR_ID_2 = 2L;
    private static final Long EXISTING_AUTHOR_ID_3 = 3L;

    private static final Long NONEXISTENT_AUTHOR_ID = 100L;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Test find all authors")
    void givenAuthors_whenFindAll_thenReturnAllAuthors() {
        var authorOne = entityManager.find(Author.class, EXISTING_AUTHOR_ID_1);
        var authorTwo = entityManager.find(Author.class, EXISTING_AUTHOR_ID_2);
        var authorThree = entityManager.find(Author.class, EXISTING_AUTHOR_ID_3);

        var actualAuthors = authorRepository.findAll();

        assertThat(actualAuthors).containsExactly(authorOne, authorTwo, authorThree);
    }

    @Test
    @DisplayName("Test find existing author by id")
    void givenId_whenFindById_thenReturnAuthor() {
        var expected = entityManager.find(Author.class, EXISTING_AUTHOR_ID_1);
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