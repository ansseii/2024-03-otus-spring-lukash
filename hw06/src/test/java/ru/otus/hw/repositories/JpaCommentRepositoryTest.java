package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaCommentRepository.class)
class JpaCommentRepositoryTest {

    private static final Long EXISTING_COMMENT_ID_1 = 1L;
    private static final Long EXISTING_COMMENT_ID_2 = 2L;

    private static final Long BOOK_ID = 1L;

    private static final Long NONEXISTENT_COMMENT_ID = 101L;

    @Autowired
    private JpaCommentRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Test find existing comment by id")
    void givenId_whenFindById_thenReturnComment() {
        var expected = entityManager.find(Comment.class, EXISTING_COMMENT_ID_1);
        var actual = repository.findById(EXISTING_COMMENT_ID_1);

        assertThat(actual).isPresent()
                .hasValue(expected);
    }

    @Test
    @DisplayName("Test find nonexistent comment by id")
    void givenIncorrectId_whenFindById_thenReturnEmpty() {
        var actual = repository.findById(NONEXISTENT_COMMENT_ID);

        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("Test find all comments by provided book id")
    void givenBookId_whenFindByBookId_thenReturnAllRelatedComments() {
        var commentOne = entityManager.find(Comment.class, EXISTING_COMMENT_ID_1);
        var commentTwo = entityManager.find(Comment.class, EXISTING_COMMENT_ID_2);

        var actual = repository.findAllByBookId(BOOK_ID);

        assertThat(actual).containsExactly(commentOne, commentTwo);
    }

    @Test
    @DisplayName("Test save new comment")
    void givenNewComment_whenSave_thenSaveNewComment() {
        var relatedBook = entityManager.find(Book.class, BOOK_ID);
        var commentToSave = new Comment(null, "NEW_TEST_COMMENT", relatedBook);

        var savedComment = repository.save(commentToSave);
        assertThat(savedComment).isNotNull()
                .matches(b -> b.getId() != null)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(commentToSave);

        var foundComment = entityManager.find(Comment.class, savedComment.getId());
        assertThat(foundComment).isEqualTo(savedComment);
    }

    @Test
    @DisplayName("Test update existing comment")
    void givenExistingComment_whenSave_thenUpdateExistingComment() {
        var updatedMessage = "UPDATED_COMMENT_MESSAGE";
        var existingComment = entityManager.find(Comment.class, EXISTING_COMMENT_ID_1);

        var commentToUpdate = new Comment(existingComment.getId(), updatedMessage, existingComment.getBook());
        var updated = repository.save(commentToUpdate);

        assertThat(updated).isNotNull()
                .matches(b -> b.getMessage().equals(updatedMessage));

        assertThat(entityManager.find(Comment.class, updated.getId())).isEqualTo(updated);
    }

    @Test
    @DisplayName("Test delete comment")
    void givenCommentId_whenDeleteById_thenDeleteComment() {
        var commentToDelete = entityManager.find(Comment.class, EXISTING_COMMENT_ID_1);
        assertThat(commentToDelete).isNotNull();

        repository.deleteById(EXISTING_COMMENT_ID_1);
        assertThat(entityManager.find(Comment.class, EXISTING_COMMENT_ID_1)).isNull();
    }
}