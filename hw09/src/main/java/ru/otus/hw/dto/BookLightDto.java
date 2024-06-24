package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.Collections;
import java.util.Set;

public record BookLightDto(
        Long bookId,
        @NotBlank(message = "Title cannot be empty") String title,
        @Positive(message = "Author must be selected") Long authorId,
        @NotEmpty(message = "At least one genre must be selected") Set<Long> genreIds) {
    public static BookLightDto empty() {
        return new BookLightDto(null, null, null, Collections.emptySet());
    }
}
