import PropTypes from 'prop-types';
import { useContext, useState } from "react";
import { Button, Modal } from "react-bootstrap";
import { StateContext } from "../context/AppContext";
import BookService from "../services/BookService";
import BookForm from "./BookForm";

const EditBookButton = ({ toEdit }) => {
    const [ show, setShow ] = useState(false);
    const [ book, setBook ] = useState(toEdit);
    const [ errors, setErrors ] = useState([]);

    const {
        books: { booksReloaded, setBooksReloaded },
        genres: { loadGenres },
        authors: { loadAuthors },
    } = useContext(StateContext);


    const handleEdit = (event) => {
        event.preventDefault();

        const data = {
            bookId: book.id,
            title: book.title,
            authorId: book.author,
            genreIds: book.genres,
        };
        BookService.editBook(book.id, data)
            .then(() => {
                setBooksReloaded(!booksReloaded);
                setShow(false);
            })
            .catch((error) => {
                console.log(error);
                setErrors(error.response.data.errors);
            });

    };

    const handleClose = () => setShow(false);
    const handleShow = () => {
        setShow(true);
        loadAuthors().catch((error) => console.log(error));
        loadGenres().catch((error) => console.log(error));
    };

    return (
        <>
            <Button variant="outline-primary" onClick={handleShow}>
                <i className="bi bi-pencil"></i>
            </Button>

            <Modal show={show}>
                <Modal.Header>
                    <Modal.Title>Edit book info</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <BookForm onSubmit={handleEdit} id="editBookForm" state={[ book, setBook ]}
                              errorsState={[ errors, setErrors ]} />
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    <Button variant="primary" form="editBookForm" type="submit">
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    )
};

EditBookButton.propTypes = {
    toEdit: PropTypes.object.isRequired,
};

export default EditBookButton;