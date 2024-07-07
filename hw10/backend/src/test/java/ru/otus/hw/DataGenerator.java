package ru.otus.hw;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataGenerator {

    public static List<BookDto> getAllBooks() {
        return List.of(
                new BookDto(1L, "BookTitle_1",
                        new AuthorDto(1L, "Author_1"),
                        List.of(
                                new GenreDto(1L, "Genre_1"),
                                new GenreDto(2L, "Genre_2")
                        )),
                new BookDto(2L, "BookTitle_2",
                        new AuthorDto(2L, "Author_2"),
                        List.of(
                                new GenreDto(3L, "Genre_3"),
                                new GenreDto(4L, "Genre_4")
                        )),
                new BookDto(3L, "BookTitle_3",
                        new AuthorDto(3L, "Author_3"),
                        List.of(
                                new GenreDto(5L, "Genre_5"),
                                new GenreDto(6L, "Genre_6")
                        ))
        );
    }

    public static List<AuthorDto> getAllAuthors() {
        return List.of(
                new AuthorDto(1L, "Author_1"),
                new AuthorDto(2L, "Author_2"),
                new AuthorDto(3L, "Author_3")
        );
    }

    public static List<GenreDto> getAllGenres() {
        return List.of(
                new GenreDto(1L, "Genre_1"),
                new GenreDto(2L, "Genre_2"),
                new GenreDto(3L, "Genre_3"),
                new GenreDto(4L, "Genre_4"),
                new GenreDto(5L, "Genre_5"),
                new GenreDto(6L, "Genre_6")
        );
    }
}
