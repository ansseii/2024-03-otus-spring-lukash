import AuthorService from "./services/AuthorService.js";
import BookService from "./services/BookService.js";
import GenreService from "./services/GenreService.js";

const retrieveBooks = (setBooks) => {
    return async () => {
        const { data } = await BookService.getAllBooks();
        setBooks(data);
    };
};

const retrieveAuthors = (setAuthors) => {
    return async () => {
        const { data } = await AuthorService.getAllAuthors();
        setAuthors(data);
    };
};

const retrieveGenres = (setGenres) => {
    return async () => {
        const { data } = await GenreService.getAllGenres();
        setGenres(data);
    };
};

export default { retrieveBooks, retrieveAuthors, retrieveGenres }