import { Link, useRouteError } from "react-router-dom";
import Container from "react-bootstrap/Container";
import { Button, Col, Row } from "react-bootstrap";

const ErrorPage = () => {
    const error = useRouteError();

    return (
        <Container className="text-center mt-5">
            <Row>
                <Col>
                    <h1 className="display-1">{error.status}</h1>
                    <h2 className="mb-4">{error.statusText}</h2>
                    <Link to="/">
                        <Button variant="primary">Go Home</Button>
                    </Link>
                </Col>
            </Row>
        </Container>
    );
}

export default ErrorPage;