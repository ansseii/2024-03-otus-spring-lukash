package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(BookDto::toString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(long id) {
        return bookService.findById(id)
                .map(BookDto::toString)
                .orElse("Book with id %d not found".formatted(id));
    }

    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, long authorId, Set<Long> genresIds) {
        var savedBook = bookService.insert(title, authorId, genresIds);
        return savedBook.toString();
    }

    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(long id, String title, long authorId, Set<Long> genresIds) {
        var savedBook = bookService.update(id, title, authorId, genresIds);
        return savedBook.toString();
    }

    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(long id) {
        bookService.deleteById(id);
    }
}
