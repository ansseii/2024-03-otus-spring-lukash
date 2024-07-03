import { Container, Row } from "react-bootstrap";
import GenreTable from "../components/GenreTable";

const Genres = () => (
    <Container className="mt-3">
        <Row>
            <GenreTable />
        </Row>
    </Container>
);

export default Genres;