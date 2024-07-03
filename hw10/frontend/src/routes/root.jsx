import { Container, Nav, Navbar, Row } from 'react-bootstrap';
import { Outlet } from "react-router-dom";

const Root = () => (
    <Container>
        <Row className="mb-4">
            <Navbar expand="lg" className="bg-body-tertiary">
                <Container>
                    <Navbar.Brand href="/">Library</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto">
                            <Nav.Link href="/books">Books</Nav.Link>
                            <Nav.Link href="/authors">Authors</Nav.Link>
                            <Nav.Link href="/genres">Genres</Nav.Link>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
        </Row>
        <Row className="mb-4">
            <Outlet />
        </Row>
    </Container>
);

export default Root;