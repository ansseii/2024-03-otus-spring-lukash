package ru.otus.hw.it;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
@Transactional(propagation = Propagation.NEVER)
class CommentServiceIT {

    private static final Long EXISTING_COMMENT_ID = 1L;
    private static final Long EXISTING_BOOK_ID = 1L;
    private static final Long NONEXISTENT_COMMENT_ID = 5L;

    @Autowired
    private CommentService commentService;

    @Test
    @DisplayName("Test find existing comment by id")
    void givenCommentId_whenFindById_thenReturnComment() {
        var expected = new CommentDto(EXISTING_COMMENT_ID, "Comment_1_1", "BookTitle_1");
        var actual = commentService.findById(EXISTING_COMMENT_ID);

        assertThat(actual).isPresent()
                .hasValue(expected);
    }

    @Test
    @DisplayName("Test find nonexistent comment by id")
    void givenIncorrectCommentId_whenFindById_thenReturnEmpty() {
        var actual = commentService.findById(NONEXISTENT_COMMENT_ID);

        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("Test find all comments by given book id")
    void givenBookId_whenFindByBookId_thenReturnAllRelatedComments() {
        var expected = List.of(
                new CommentDto(1L, "Comment_1_1", "BookTitle_1"),
                new CommentDto(2L, "Comment_1_2", "BookTitle_1")
        );
        var actual = commentService.findAllByBookId(EXISTING_BOOK_ID);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DirtiesContext
    @DisplayName("Test create new comment")
    void givenNewComment_whenInsert_thenReturnCreatedComment() {
        var expected = new CommentDto(NONEXISTENT_COMMENT_ID, "Test comment created", "BookTitle_1");

        assertThat(commentService.findAllByBookId(EXISTING_BOOK_ID)).hasSize(2);

        commentService.insert("Test comment created", EXISTING_BOOK_ID);
        assertThat(commentService.findAllByBookId(EXISTING_BOOK_ID)).hasSize(3);

        var actual = commentService.findById(NONEXISTENT_COMMENT_ID);
        assertThat(actual).isPresent()
                .hasValue(expected);
    }

    @Test
    @DirtiesContext
    @DisplayName("Test update existing comment")
    void givenExistingComment_whenUpdate_thenReturnUpdatedComment() {
        var expected = new CommentDto(EXISTING_COMMENT_ID, "Test comment updated", "BookTitle_1");

        assertThat(commentService.findAllByBookId(EXISTING_BOOK_ID)).hasSize(2);

        commentService.update(EXISTING_COMMENT_ID, "Test comment updated", EXISTING_BOOK_ID);
        assertThat(commentService.findAllByBookId(EXISTING_BOOK_ID)).hasSize(2);

        var actual = commentService.findById(EXISTING_COMMENT_ID);
        assertThat(actual).isPresent()
                .hasValue(expected);
    }

    @Test
    @DirtiesContext
    @DisplayName("Test delete existing comment")
    void givenExistingComment_whenDelete_thenDeleteComment() {
        assertThat(commentService.findAllByBookId(EXISTING_BOOK_ID)).hasSize(2);

        commentService.deleteById(EXISTING_COMMENT_ID);
        assertThat(commentService.findAllByBookId(EXISTING_BOOK_ID)).hasSize(1);
        assertThat(commentService.findById(EXISTING_COMMENT_ID)).isEmpty();
    }
}
