import PropTypes from 'prop-types';
import { createContext, useMemo, useState } from "react";
import DataLoader from "../DataLoader";

export const StateContext = createContext(null);

const AppContext = ({ children }) => {
    const [ books, setBooks ] = useState([]);
    const [ booksReloaded, setBooksReloaded ] = useState(true);

    const [ genres, setGenres ] = useState([]);
    const [ genresReloaded, setGenresReloaded ] = useState(true);

    const [ authors, setAuthors ] = useState([]);
    const [ authorsReloaded, setAuthorsReloaded ] = useState(true);

    const context = useMemo(() => (
        {
            books: {
                books, setBooks,
                loadBooks: DataLoader.retrieveBooks(setBooks),
                booksReloaded, setBooksReloaded
            },
            genres: {
                genres, setGenres,
                loadGenres: DataLoader.retrieveGenres(setGenres),
                genresReloaded, setGenresReloaded
            },
            authors: {
                authors, setAuthors,
                loadAuthors: DataLoader.retrieveAuthors(setAuthors),
                authorsReloaded, setAuthorsReloaded
            },
        }
    ), [
        books, booksReloaded,
        genres, genresReloaded,
        authors, authorsReloaded,
    ]);

    return (
        <StateContext.Provider value={context}>
            {children}
        </StateContext.Provider>
    );
};

AppContext.propTypes = {
    children: PropTypes.node.isRequired,
};

export default AppContext;