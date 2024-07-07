package ru.otus.hw.dto;

public record AuthorDto(Long id, String fullName) {
    @Override
    public String toString() {
        return "Id: %d, FullName: %s".formatted(id, fullName);
    }
}
