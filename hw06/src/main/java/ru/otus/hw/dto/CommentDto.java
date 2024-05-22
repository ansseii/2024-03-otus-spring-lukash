package ru.otus.hw.dto;

public record CommentDto(Long id, String message, String bookTitle) {
    @Override
    public String toString() {
        return "Id: %d, Name: %s, Book title: %s".formatted(id, message, bookTitle);
    }
}
