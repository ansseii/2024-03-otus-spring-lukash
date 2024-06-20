package ru.otus.hw.dto;

public record CommentDto(String id, String message, String bookTitle) {
    @Override
    public String toString() {
        return "Id: %s, Name: %s, Book title: %s".formatted(id, message, bookTitle);
    }
}
