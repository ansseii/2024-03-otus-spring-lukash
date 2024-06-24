package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookLightDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private static final String BOOK_MODEL_ATTRIBUTE = "book";
    private static final String BOOKS_MODEL_ATTRIBUTE = "books";
    private static final String GENRES_MODEL_ATTRIBUTE = "genres";
    private static final String AUTHORS_MODEL_ATTRIBUTE = "authors";
    private static final String REDIRECT_BOOK_LIST = "redirect:/books";

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookConverter bookConverter;

    @GetMapping
    public String getAllBooks(Model model) {
        model.addAttribute(BOOKS_MODEL_ATTRIBUTE, bookService.findAll());
        return "books-view";
    }

    @GetMapping("/create")
    public String getCreateBookPage(Model model) {

        model.addAttribute(GENRES_MODEL_ATTRIBUTE, genreService.findAll());
        model.addAttribute(AUTHORS_MODEL_ATTRIBUTE, authorService.findAll());
        model.addAttribute(BOOK_MODEL_ATTRIBUTE, BookLightDto.empty());

        return "create-book-view";
    }

    @GetMapping("/{id}/edit")
    public String getEditBookPage(@PathVariable long id, Model model) {
        var book = bookService.findById(id).orElseThrow(EntityNotFoundException::new);

        model.addAttribute(GENRES_MODEL_ATTRIBUTE, genreService.findAll());
        model.addAttribute(AUTHORS_MODEL_ATTRIBUTE, authorService.findAll());
        model.addAttribute(BOOK_MODEL_ATTRIBUTE, bookConverter.toLightDto(book));

        return "edit-book-view";
    }

    @PostMapping
    public String saveBook(@Valid @ModelAttribute("book") BookLightDto toSave, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(GENRES_MODEL_ATTRIBUTE, genreService.findAll());
            model.addAttribute(AUTHORS_MODEL_ATTRIBUTE, authorService.findAll());
            return "create-book-view";
        }

        bookService.insert(toSave.title(), toSave.authorId(), toSave.genreIds());
        return REDIRECT_BOOK_LIST;
    }

    @PutMapping("/{id}")
    public String updateBook(@PathVariable long id, @Valid @ModelAttribute("book") BookLightDto toUpdate,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(GENRES_MODEL_ATTRIBUTE, genreService.findAll());
            model.addAttribute(AUTHORS_MODEL_ATTRIBUTE, authorService.findAll());
            return "edit-book-view";
        }

        bookService.update(id, toUpdate.title(), toUpdate.authorId(), toUpdate.genreIds());
        return REDIRECT_BOOK_LIST;
    }

    @DeleteMapping(value = "/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return REDIRECT_BOOK_LIST;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleNotFound() {
        return new ModelAndView("not-found-view", HttpStatus.NOT_FOUND);
    }
}
