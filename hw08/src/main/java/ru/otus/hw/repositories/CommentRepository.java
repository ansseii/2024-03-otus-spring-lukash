package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findAllByBookId(String bookId);

    void deleteAllByBookId(String bookId);
}
