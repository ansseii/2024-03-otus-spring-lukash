package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class MongoDbCommentRepositoryTest {

    private static final String EXISTING_COMMENT_ID_1 = "1";
    private static final String EXISTING_COMMENT_ID_2 = "2";

    private static final String BOOK_ID = "1";

    private static final String NONEXISTENT_COMMENT_ID = "101";

    @Autowired
    private CommentRepository repository;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    @DisplayName("Test find existing comment by id")
    void givenId_whenFindById_thenReturnComment() {
        var expected = mongoOperations.findById(EXISTING_COMMENT_ID_1, Comment.class);
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
        var commentOne = mongoOperations.findById(EXISTING_COMMENT_ID_1, Comment.class);
        var commentTwo = mongoOperations.findById(EXISTING_COMMENT_ID_2, Comment.class);

        var actual = repository.findAllByBookId(BOOK_ID);

        assertThat(actual).containsExactly(commentOne, commentTwo);
    }

    @Test
    @DirtiesContext
    @DisplayName("Test save new comment")
    void givenNewComment_whenSave_thenSaveNewComment() {
        var relatedBook = mongoOperations.findById(BOOK_ID, Book.class);
        var commentToSave = new Comment(null, "NEW_TEST_COMMENT", relatedBook);

        var savedComment = repository.save(commentToSave);
        assertThat(savedComment).isNotNull()
                .matches(b -> b.getId() != null)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(commentToSave);

        var foundComment = mongoOperations.findById(savedComment.getId(), Comment.class);
        assertThat(foundComment).isEqualTo(savedComment);
    }

    @Test
    @DirtiesContext
    @DisplayName("Test update existing comment")
    void givenExistingComment_whenSave_thenUpdateExistingComment() {
        var updatedMessage = "UPDATED_COMMENT_MESSAGE";
        var existingComment = mongoOperations.findById(EXISTING_COMMENT_ID_1, Comment.class);

        var commentToUpdate = new Comment(existingComment.getId(), updatedMessage, existingComment.getBook());
        var updated = repository.save(commentToUpdate);

        assertThat(updated).isNotNull()
                .matches(b -> b.getMessage().equals(updatedMessage));

        assertThat(mongoOperations.findById(updated.getId(), Comment.class)).isEqualTo(updated);
    }

    @Test
    @DirtiesContext
    @DisplayName("Test delete comment")
    void givenCommentId_whenDeleteById_thenDeleteComment() {
        var commentToDelete = mongoOperations.findById(EXISTING_COMMENT_ID_1, Comment.class);
        assertThat(commentToDelete).isNotNull();

        repository.deleteById(EXISTING_COMMENT_ID_1);
        assertThat(mongoOperations.findById(EXISTING_COMMENT_ID_1, Comment.class)).isNull();
    }
}