package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

@Component
@RequiredArgsConstructor
public class CommentConverter {

    public CommentDto commentToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getMessage(), comment.getBook().getTitle());
    }
}
