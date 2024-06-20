package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentConverter commentConverter;

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDto> findById(String id) {
        return commentRepository.findById(id).map(commentConverter::commentToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAllByBookId(String id) {
        return commentRepository.findAllByBookId(id).stream()
                .map(commentConverter::commentToDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentDto insert(String message, String bookId) {
        return save(null, message, bookId);
    }

    @Override
    @Transactional
    public CommentDto update(String id, String message, String bookId) {
        return save(id, message, bookId);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    private CommentDto save(String id, String message, String bookId) {
        var relatedBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        var comment = new Comment(id, message, relatedBook);

        return commentConverter.commentToDto(commentRepository.save(comment));
    }
}
