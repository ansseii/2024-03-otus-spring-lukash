import { useContext, useState } from "react";
import { Button, Modal } from "react-bootstrap";
import { StateContext } from "../context/AppContext";
import BookService from "../services/BookService";
import BookForm from "./BookForm";

const AddNewBookButton = () => {
    const [ book, setBook ] = useState({ genres: [] });
    const [ modalShow, setModalShow ] = useState(false);
    const [ errors, setErrors ] = useState([]);

    const {
        books: { booksReloaded, setBooksReloaded },
        genres: { loadGenres },
        authors: { loadAuthors },
    } = useContext(StateContext);

    const handleSave = (event) => {
        event.preventDefault();

        const data = {
            title: book.title,
            authorId: book.author,
            genreIds: book.genres,
        };
        BookService.addBook(data)
            .then(() => {
                setBooksReloaded(!booksReloaded);
                setModalShow(false);
            })
            .catch((error) => {
                console.log(error);
                setErrors(error.response.data.errors);
            });

    };

    const handleClose = () => setModalShow(false);
    const handleShow = () => {
        setModalShow(true);
        loadAuthors().catch((error) => console.log(error));
        loadGenres().catch((error) => console.log(error));
    };

    return (
        <>
            <Button variant="outline-secondary" onClick={handleShow}>Add new book</Button>

            <Modal
                show={modalShow}
                size="lg"
                aria-labelledby="contained-modal-title-vcenter"
                centered
            >
                <Modal.Header>
                    <Modal.Title id="contained-modal-title-vcenter">
                        Add new book
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <BookForm onSubmit={handleSave} id="addNewBookForm" state={[ book, setBook ]}
                              errorsState={[ errors, setErrors ]} />
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    <Button variant="primary" form="addNewBookForm" type="submit">
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
};

export default AddNewBookButton;