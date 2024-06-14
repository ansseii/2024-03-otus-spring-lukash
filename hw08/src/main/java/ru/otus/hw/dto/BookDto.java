package ru.otus.hw.dto;

import java.util.List;

public record BookDto(String id, String title, AuthorDto author, List<GenreDto> genres) {
    @Override
    public String toString() {
        return "Id: %s, title: %s, author: {%s}, genres: %s".formatted(id, title, author, genres);
    }
}
