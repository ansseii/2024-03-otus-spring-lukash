package ru.otus.hw.dto;

public record GenreDto(Long id, String name) {
    @Override
    public String toString() {
        return "Id: %d, Name: %s".formatted(id, name);
    }
}
