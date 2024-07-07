package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookLightDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Book;

import java.util.stream.Collectors;

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

    public BookLightDto toLightDto(BookDto bookDto) {
        var genres = bookDto.genres().stream()
                .map(GenreDto::id)
                .collect(Collectors.toSet());

        return new BookLightDto(bookDto.id(), bookDto.title(), bookDto.author().id(), genres);
    }
}
