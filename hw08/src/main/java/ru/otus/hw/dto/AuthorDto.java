package ru.otus.hw.dto;

public record AuthorDto(String id, String fullName) {
    @Override
    public String toString() {
        return "Id: %s, FullName: %s".formatted(id, fullName);
    }
}
