package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

@Component
@RequiredArgsConstructor
public class BookConverter {

    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public BookDto bookToDto(Book book) {
        var genres = book.getGenres().stream()
                .map(genreConverter::genreToDto)
                .toList();

        return new BookDto(book.getId(), book.getTitle(),
                authorConverter.authorToDto(book.getAuthor()), genres);
    }
}
