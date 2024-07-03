import { useContext, useEffect } from "react";
import { Col, Container, Row, Table } from "react-bootstrap";
import BookTableItem from "../components/BookTableItem";
import { StateContext } from "../context/AppContext";
import AddNewBookButton from "./AddNewBookButton";

const BookTable = () => {
    const {
        books: { books, loadBooks, booksReloaded },
        genres: { loadGenres },
        authors: { loadAuthors },
    } = useContext(StateContext);

    useEffect(() => {
        loadBooks().catch((error) => console.log(error));
    }, [ booksReloaded ]);

    useEffect(() => {
        loadGenres().catch((error) => console.log(error));
        loadAuthors().catch((error) => console.log(error));
    }, []);

    return (
        <Container>
            <Row className="justify-content-start mb-4">
                <Col>
                    <AddNewBookButton />
                </Col>
            </Row>
            <Row>
                <Table striped bordered hover>
                    <thead>
                    <tr className="text-center">
                        <th scope="col">Title</th>
                        <th scope="col">Author</th>
                        <th scope="col" colSpan={2}>Genres</th>
                    </tr>
                    </thead>
                    <tbody>
                    {books.map((book) => <BookTableItem key={book.id} book={book} />)}
                    </tbody>
                </Table>
            </Row>
        </Container>
    );
};

export default BookTable;