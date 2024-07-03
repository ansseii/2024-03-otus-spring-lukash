import PropTypes from 'prop-types';
import { useContext } from "react";
import { Button, ButtonGroup, ListGroup, ListGroupItem } from "react-bootstrap";
import { StateContext } from "../context/AppContext";
import BookService from "../services/BookService";
import EditBookButton from "./EditBookButton";

const BookTableItem = ({ book }) => {
    const { title, author: { fullName }, genres } = book;
    const {
        books: { booksReloaded, setBooksReloaded },
    } = useContext(StateContext);

    const handleDelete = (id) => {
        BookService.deleteBook(id)
            .then(() => setBooksReloaded(!booksReloaded))
            .catch(err => console.log(err));
    }

    return (
        <tr>
            <td className="text-center">{title}</td>
            <td className="text-center">{fullName}</td>
            <td>
                <ListGroup horizontal>
                    {genres.map(({ id, name }) =>
                        <ListGroupItem variant="dark" key={id}>{name}</ListGroupItem>)}
                </ListGroup>
            </td>
            <td className="text-center">
                <ButtonGroup>
                    <EditBookButton
                        toEdit={{ ...book, author: book.author.id, genres: book.genres.map(({ id }) => id) }} />
                    <Button variant="outline-danger" onClick={() => handleDelete(book.id)}>
                        <i className="bi bi-trash"></i>
                    </Button>
                </ButtonGroup>
            </td>
        </tr>
    );
};

BookTableItem.propTypes = {
    book: PropTypes.object.isRequired,
};

export default BookTableItem;