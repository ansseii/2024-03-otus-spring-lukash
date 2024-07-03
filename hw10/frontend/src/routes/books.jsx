import { Container, Row } from "react-bootstrap";
import BookTable from "../components/BookTable";

const Books = () => (
    <Container className="mt-3">
        <Row>
            <BookTable />
        </Row>
    </Container>
);

export default Books;