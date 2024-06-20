package ru.otus.hw.dto;

public record GenreDto(String id, String name) {
    @Override
    public String toString() {
        return "Id: %s, Name: %s".formatted(id, name);
    }
}
