package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<CommentDto> findById(long id);

    List<CommentDto> findAllByBookId(long id);

    CommentDto insert(String message, long bookId);

    CommentDto update(long id, String message, long bookId);

    void deleteById(long id);
}
