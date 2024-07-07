import { Col, Row } from "react-bootstrap";
import Container from "react-bootstrap/Container";

const GreetingPage = () => {
    return (
        <Container className="text-center mt-5">
            <Row>
                <Col>
                    <h2 className="display-2">Welcome to the Library App</h2>
                    <p className="mt-5 display-6">We're delighted to have you here. Browse through our extensive
                        collection of available books and
                        find your next great read. Happy exploring! 📚✨</p>
                </Col>
            </Row>
        </Container>
    );
}

export default GreetingPage;