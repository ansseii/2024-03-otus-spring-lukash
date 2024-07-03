import { Container, Row } from "react-bootstrap";
import AuthorTable from "../components/AuthorTable";

const Authors = () => (
    <Container className="mt-3">
        <Row>
            <AuthorTable />
        </Row>
    </Container>
);

export default Authors;