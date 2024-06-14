package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<CommentDto> findById(String id);

    List<CommentDto> findAllByBookId(String id);

    CommentDto insert(String message, String bookId);

    CommentDto update(String id, String message, String bookId);

    void deleteById(String id);
}
