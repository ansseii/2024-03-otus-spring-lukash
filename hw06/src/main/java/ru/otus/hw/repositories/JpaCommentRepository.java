package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hibernate.jpa.SpecHints.HINT_SPEC_FETCH_GRAPH;

@Repository
@RequiredArgsConstructor
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Comment> findById(long id) {
        var graph = em.getEntityGraph("comment-entity-graph");
        return Optional.ofNullable(em.find(Comment.class, id, Map.of(HINT_SPEC_FETCH_GRAPH, graph)));
    }

    @Override
    public List<Comment> findAllByBookId(long bookId) {
        var graph = em.getEntityGraph("comment-entity-graph");
        return em.createQuery("select c from Comment c where c.book.id = :bookId", Comment.class)
                .setParameter("bookId", bookId)
                .setHint(HINT_SPEC_FETCH_GRAPH, graph)
                .getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            em.persist(comment);
            return comment;
        }

        return em.merge(comment);
    }

    @Override
    public void deleteById(long id) {
        var comment = em.find(Comment.class, id);
        if (comment != null) {
            em.remove(comment);
        }
    }
}
